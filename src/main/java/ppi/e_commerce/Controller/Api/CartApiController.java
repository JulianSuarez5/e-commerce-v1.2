package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.AddCartItemRequest;
import ppi.e_commerce.Dto.CartDto;
import ppi.e_commerce.Exception.BusinessException;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Service.CartService;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartApiController {

    private final CartService cartService;

    @Autowired
    public CartApiController(CartService cartService) {
        this.cartService = cartService;
    }

    private String getUsername(Authentication authentication) {
        if (authentication == null) {
            throw new AccessDeniedException("Authentication is required to access the cart.");
        }
        return authentication.getName();
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication authentication) {
        String username = getUsername(authentication);
        CartDto cartDto = cartService.getCartDtoByUsername(username);
        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(@Valid @RequestBody AddCartItemRequest request, Authentication authentication) {
        String username = getUsername(authentication);
        CartDto updatedCart = cartService.addItemToCart(username, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDto> updateItem(@PathVariable Long itemId, @RequestParam Integer quantity, Authentication authentication) {
        String username = getUsername(authentication);
        CartDto updatedCart = cartService.updateItemInCart(username, itemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDto> removeItem(@PathVariable Long itemId, Authentication authentication) {
        String username = getUsername(authentication);
        CartDto updatedCart = cartService.removeItemFromCart(username, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        String username = getUsername(authentication);
        cartService.clearCartByUsername(username);
        log.info("Cart cleared for user '{}'", username);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedError(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
