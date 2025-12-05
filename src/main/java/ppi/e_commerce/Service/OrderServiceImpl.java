package ppi.e_commerce.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Dto.OrderTrackingDto;
import ppi.e_commerce.Dto.UpdateOrderStatusRequest;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.OrderMapper;
import ppi.e_commerce.Model.*;
import ppi.e_commerce.Repository.OrderRepository;
import ppi.e_commerce.Repository.OrderTrackingRepository;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Model.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderTrackingRepository orderTrackingRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        List<Order> orders = orderRepository.findByUserOrderByCreationDateDesc(user);
        return orderMapper.toDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDto> findOrderById(Integer orderId, String username) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    if (!order.getUser().getUsername().equals(username)) {
                        throw new AccessDeniedException("You do not have permission to view this order.");
                    }
                    return orderMapper.toDto(order);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderTrackingDto> findOrderTracking(Integer orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with ID: " + orderId);
        }
        List<OrderTracking> tracking = orderTrackingRepository.findByOrderIdOrderByTimestampAsc(orderId);
        // This mapping can be done with a dedicated OrderTrackingMapper if desired
        return tracking.stream().map(this::toTrackingDto).toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatusAndTrack(Integer orderId, UpdateOrderStatusRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only admins can update order status.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(request.getStatus());

        OrderTracking tracking = new OrderTracking();
        tracking.setOrder(order);
        tracking.setStatus(request.getStatus());
        tracking.setDescription(request.getDescription());
        tracking.setLocation(request.getLocation());
        tracking.setTrackingNumber(request.getTrackingNumber());
        tracking.setTimestamp(LocalDateTime.now());
        orderTrackingRepository.save(tracking);

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    // Helper for tracking DTO, can be moved to a mapper
    private OrderTrackingDto toTrackingDto(OrderTracking tracking) {
        OrderTrackingDto dto = new OrderTrackingDto();
        dto.setId(tracking.getId());
        dto.setStatus(tracking.getStatus());
        dto.setDescription(tracking.getDescription());
        dto.setLocation(tracking.getLocation());
        dto.setTrackingNumber(tracking.getTrackingNumber());
        dto.setTimestamp(tracking.getTimestamp());
        return dto;
    }

    @Override
    public Order createOrder(User user, List<CartItem> cartItems) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreationDateDesc(user);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Long countOrdersByUser(User user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Double getTotalSales() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Order> getRecentOrders(int limit) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Long countOrders() {
        return orderRepository.count();
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
