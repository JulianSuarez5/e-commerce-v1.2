package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Dto.OrderTrackingDto;
import ppi.e_commerce.Dto.UpdateOrderStatusRequest;
import ppi.e_commerce.Model.CartItem;
import ppi.e_commerce.Model.Order;
import ppi.e_commerce.Model.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    // --- New DTO-based Service Methods for API consumption ---

    List<OrderDto> findUserOrders(String username);

    Optional<OrderDto> findOrderById(Integer orderId, String username);

    List<OrderTrackingDto> findOrderTracking(Integer orderId);

    OrderDto updateOrderStatusAndTrack(Integer orderId, UpdateOrderStatusRequest request, String username);


    // --- Existing methods (primarily for internal use or to be phased out) ---

    Order createOrder(User user, List<CartItem> cartItems);

    /** @deprecated Use {@link #findOrderById(Integer, String)} which returns DTO and includes security checks. */
    @Deprecated
    Optional<Order> getOrderById(Integer id);

    /** @deprecated Use {@link #findUserOrders(String)} which returns DTOs. */
    @Deprecated
    List<Order> getOrdersByUser(User user);

    List<Order> getAllOrders();

    /** @deprecated Use {@link #updateOrderStatusAndTrack(Integer, UpdateOrderStatusRequest, String)} for transactional updates. */
    @Deprecated
    Order updateOrderStatus(Integer orderId, String status);

    Order updateOrder(Order order);

    void deleteOrder(Integer orderId);

    List<Order> getOrdersByStatus(String status);

    Long countOrdersByUser(User user);

    Double getTotalSales();

    List<Order> getRecentOrders(int limit);

    Long countOrders();

    List<Order> findAll();
}
