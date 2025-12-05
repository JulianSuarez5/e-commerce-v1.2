package ppi.e_commerce.Service;

import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.CartDto;
import ppi.e_commerce.Model.Cart;
import ppi.e_commerce.Model.CartItem;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.User;

import java.util.List;

public interface CartService {

    // --- New, Preferred Methods ---

    @Transactional(readOnly = true)
    CartDto getCartDtoByUsername(String username);

    @Transactional
    CartDto addItemToCart(String username, Integer productId, Integer quantity);

    @Transactional
    CartDto updateItemInCart(String username, Long cartItemId, Integer quantity);

    @Transactional
    CartDto removeItemFromCart(String username, Long cartItemId);

    @Transactional
    void clearCartByUsername(String username);

    // --- Existing Methods (some to be deprecated) ---

    Cart getOrCreateCart(User user);

    /** @deprecated Use {@link #getCartDtoByUsername(String)} instead. */
    @Deprecated
    Cart getOrCreateCart(Integer userId);

    /** @deprecated Use {@link #addItemToCart(String, Integer, Integer)} instead. */
    @Deprecated
    CartItem addToCart(User user, Product product, Integer quantity);

    /** @deprecated Use {@link #addItemToCart(String, Integer, Integer)} instead. */
    @Deprecated
    CartItem addItem(Integer cartId, Integer productId, Integer quantity);

    /** @deprecated Use {@link #updateItemInCart(String, Long, Integer)} instead. */
    @Deprecated
    CartItem updateCartItem(Integer cartItemId, Integer quantity);

    /** @deprecated Use {@link #removeItemFromCart(String, Long)} instead. */
    @Deprecated
    void removeFromCart(Integer cartItemId);

    /** @deprecated Use {@link #clearCartByUsername(String)} instead. */
    @Deprecated
    void clearCart(User user);

    /** @deprecated Use {@link #clearCartByUsername(String)} instead. */
    @Deprecated
    void clearCart(Integer userId);

    /** @deprecated CartDto now contains items. Fetch via getCartDtoByUsername. */
    @Deprecated
    List<CartItem> getCartItems(User user);

    /** @deprecated CartDto now contains total price. Fetch via getCartDtoByUsername. */
    @Deprecated
    double getCartTotal(User user);

    /** @deprecated CartDto now contains total items. Fetch via getCartDtoByUsername. */
    @Deprecated
    int getCartItemCount(User user);

    /** @deprecated Logic should be handled within the service layer. */
    @Deprecated
    boolean isProductInCart(User user, Product product);
}
