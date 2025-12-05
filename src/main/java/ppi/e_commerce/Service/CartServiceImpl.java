package ppi.e_commerce.Service;

import ppi.e_commerce.Model.Cart;
import ppi.e_commerce.Model.CartItem;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.CartItemRepository;
import ppi.e_commerce.Repository.CartRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    @org.springframework.transaction.annotation.Transactional
    public Cart getOrCreateCart(User user) {
        Optional<Cart> existingCart = cartRepository.findByUser(user);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        Cart newCart = new Cart(user);
        return cartRepository.save(newCart);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Cart getOrCreateCart(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return getOrCreateCart(user);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
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
    @org.springframework.transaction.annotation.Transactional
    public CartItem updateCartItem(Integer cartItemId, Integer quantity) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }
        return null;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void removeFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void clearCart(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        clearCart(user);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public CartItem addItem(Integer cartId, Integer productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return addToCart(cart.getUser(), product, quantity);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CartItem> getCartItems(User user) {
        Cart cart = getOrCreateCart(user);
        return cartItemRepository.findCartItemsByCartOrderByAddedAt(cart);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public double getCartTotal(User user) {
        Cart cart = getOrCreateCart(user);
        Double total = cartItemRepository.getTotalPriceInCart(cart);
        return total != null ? total : 0.0;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public int getCartItemCount(User user) {
        Cart cart = getOrCreateCart(user);
        Integer count = cartItemRepository.getTotalItemsInCart(cart);
        return count != null ? count : 0;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public boolean isProductInCart(User user, Product product) {
        Cart cart = getOrCreateCart(user);
        return cartItemRepository.findByCartAndProduct(cart, product).isPresent();
    }
}