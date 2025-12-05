package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CartDto;
import ppi.e_commerce.Dto.CartItemDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Cart;
import ppi.e_commerce.Model.CartItem;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.User;
// import ppi.e_commerce.Repository.CartRepository;
import ppi.e_commerce.Repository.CartItemRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Service.CartService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartApiController {

    @Autowired
    private CartService cartService;

    // @Autowired
    // private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Cart cart = cartService.getOrCreateCart(user.getId());
        return ResponseEntity.ok(toDto(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            @Valid @RequestBody AddCartItemRequest request,
            Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            if (!product.isActive() || product.getCantidad() < request.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Producto no disponible o stock insuficiente");
            }

            cartService.addToCart(user, product, request.getQuantity());
            Cart updatedCart = cartService.getOrCreateCart(user);
            return ResponseEntity.ok(toDto(updatedCart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al agregar item: " + e.getMessage());
        }
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateItem(
            @PathVariable Integer itemId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            CartItem item = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));

            String username = authentication.getName();
            if (!item.getCart().getUser().getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            if (quantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }

            User user = userRepository.findByUsername(username).orElseThrow();
            Cart cart = cartService.getOrCreateCart(user.getId());
            return ResponseEntity.ok(toDto(cart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar item: " + e.getMessage());
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Integer itemId,
            Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            CartItem item = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));

            String username = authentication.getName();
            if (!item.getCart().getUser().getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            cartItemRepository.delete(item);

            User user = userRepository.findByUsername(username).orElseThrow();
            Cart cart = cartService.getOrCreateCart(user.getId());
            return ResponseEntity.ok(toDto(cart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar item: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            cartService.clearCart(user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al vaciar carrito: " + e.getMessage());
        }
    }

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setUsername(cart.getUser().getUsername());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setTotalItems(cart.getTotalItems());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        List<CartItemDto> items = cart.getCartItems().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
        dto.setItems(items);

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

    // Inner class para request
    public static class AddCartItemRequest {

        private Integer productId;
        private Integer quantity;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
