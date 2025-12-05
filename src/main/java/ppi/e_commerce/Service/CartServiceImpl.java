package ppi.e_commerce.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.CartDto;
import ppi.e_commerce.Dto.CartItemDto;
import ppi.e_commerce.Exception.BusinessException;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Cart;
import ppi.e_commerce.Model.CartItem;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.CartItemRepository;
import ppi.e_commerce.Repository.CartRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public CartDto getCartDtoByUsername(String username) {
        log.info("Fetching cart for user '{}'", username);
        User user = findUserByUsername(username);
        Cart cart = getOrCreateCart(user);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(String username, Integer productId, Integer quantity) {
        log.info("User '{}' is adding product ID '{}' (quantity: {}) to cart.", username, productId, quantity);

        User user = findUserByUsername(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!product.isActive() || product.getCantidad() < quantity) {
            log.warn("Product ID '{}' is not available or has insufficient stock for user '{}'.", productId, username);
            throw new BusinessException("Product not available or insufficient stock");
        }

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            log.info("Updated quantity for product ID '{}' in cart for user '{}'.", productId, username);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity, product.getPrice());
            cartItemRepository.save(newItem);
            log.info("Added new product ID '{}' to cart for user '{}'.", productId, username);
        }
        
        Cart updatedCart = getOrCreateCart(user);
        return toDto(updatedCart);
    }

    @Override
    @Transactional
    public CartDto updateItemInCart(String username, Long cartItemId, Integer quantity) {
        log.info("User '{}' is updating cart item ID '{}' to quantity: {}.", username, cartItemId, quantity);
        User user = findUserByUsername(username);
        CartItem item = findCartItemById(cartItemId.intValue());

        validateCartItemOwnership(user, item);

        if (quantity <= 0) {
            log.info("Quantity is <= 0, removing item ID '{}' from cart of user '{}'.", cartItemId, username);
            cartItemRepository.delete(item);
        } else {
            Product product = item.getProduct();
            if (!product.isActive() || product.getCantidad() < quantity) {
                log.warn("Insufficient stock for product ID '{}' when updating cart for user '{}'.", product.getId(), username);
                throw new BusinessException("Insufficient stock.");
            }
            item.setQuantity(quantity);
            cartItemRepository.save(item);
            log.info("Cart item ID '{}' updated to quantity {} for user '{}'.", cartItemId, quantity, username);
        }

        Cart cart = getOrCreateCart(user);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartDto removeItemFromCart(String username, Long cartItemId) {
        log.info("User '{}' is removing item ID '{}' from their cart.", username, cartItemId);
        User user = findUserByUsername(username);
        CartItem item = findCartItemById(cartItemId.intValue());
        
        validateCartItemOwnership(user, item);

        cartItemRepository.delete(item);
        log.info("Item ID '{}' removed successfully from cart of user '{}'.", cartItemId, username);

        Cart cart = getOrCreateCart(user);
        return toDto(cart);
    }

    @Override
    @Transactional
    public void clearCartByUsername(String username) {
        log.info("User '{}' is clearing their cart.", username);
        User user = findUserByUsername(username);
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
        log.info("Cart cleared successfully for user '{}'.", username);
    }
    
    @Override
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        });
    }

    @Override
    @Transactional
    public void clearCart(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        clearCartByUsername(user.getUsername());
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private CartItem findCartItemById(Integer cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + cartItemId));
    }

    private void validateCartItemOwnership(User user, CartItem item) {
        if (!item.getCart().getUser().getId().equals(user.getId())) {
            log.warn("User '{}' attempted to modify a cart item (ID: {}) that does not belong to them.", user.getUsername(), item.getId());
            throw new AccessDeniedException("You do not have permission to modify this item.");
        }
    }

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setUsername(cart.getUser().getUsername());
        
        List<CartItem> items = cart.getCartItems();
        int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();
        double totalPrice = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

        dto.setTotalPrice(totalPrice);
        dto.setTotalItems(totalItems);
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        List<CartItemDto> itemDtos = items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    private CartItemDto toItemDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductImageUrl(item.getProduct().getImageUrl());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getPrice() * item.getQuantity());
        return dto;
    }

    @Override
    public boolean isProductInCart(User user, Product product) {
        Cart cart = getOrCreateCart(user);
        return cartItemRepository.findByCartAndProduct(cart, product).isPresent();
    }

    @Override
    public int getCartItemCount(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public BigDecimal getCartTotal(User user) {
        Cart cart = getOrCreateCart(user);
        return BigDecimal.valueOf(cart.getCartItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum());
    }

    @Override
    public List<CartItem> getCartItems(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getCartItems();
    }

    // --- Deprecated / Old Methods ---
    // The methods below are kept for retro-compatibility but should be phased out.

    @Override
    @Transactional
    public Cart getOrCreateCart(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return getOrCreateCart(user);
    }
    
    @Override
    @Transactional
    public CartItem addToCart(User user, Product product, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity, product.getPrice());
            return cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional
    public CartItem updateCartItem(Integer cartItemId, Integer quantity) {
        // This method is problematic: no ownership check.
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }
        return null;
    }

    @Override
    @Transactional
    public void removeFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        clearCartByUsername(user.getUsername());
    }

    @Override
    @Transactional
    public CartItem addItem(Integer cartId, Integer productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return addToCart(cart.getUser(), product, quantity);
    }
}
