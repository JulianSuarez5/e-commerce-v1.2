package ppi.e_commerce.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.CheckoutRequest;
import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Exception.BusinessException;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.OrderMapper;
import ppi.e_commerce.Model.*;
import ppi.e_commerce.Repository.OrderRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDto processCheckout(String username, CheckoutRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Cart cart = cartService.getOrCreateCart(user);
        List<CartItem> items = cart.getCartItems();

        if (items.isEmpty()) {
            throw new BusinessException("The cart is empty.");
        }

        // Create and populate the Order
        Order order = createOrder(user, request, items);

        // Process order details and update product stock
        processOrderDetails(order, items);

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(user);

        // The payment simulation part is omitted as it was not fully implemented
        return orderMapper.toDto(savedOrder);
    }

    private Order createOrder(User user, CheckoutRequest request, List<CartItem> items) {
        double total = calculateTotal(items);

        Order order = new Order();
        order.setNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUser(user);
        order.setTotalPrice(total);
        order.setStatus("CREATED");
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingCity(request.getShippingCity());
        order.setShippingState(request.getShippingState());
        order.setShippingZipCode(request.getShippingZipCode());
        order.setNotes(request.getNotes());
        order.setCreationDate(LocalDateTime.now());
        order.setOrderDetails(new java.util.ArrayList<>());
        return order;
    }

    private double calculateTotal(List<CartItem> items) {
        return items.stream()
                .mapToDouble(item -> {
                    validateProductAvailability(item.getProduct(), item.getQuantity());
                    return item.getPrice() * item.getQuantity();
                })
                .sum();
    }

    private void processOrderDetails(Order order, List<CartItem> items) {
        for (CartItem cartItem : items) {
            Product product = cartItem.getProduct();
            validateProductAvailability(product, cartItem.getQuantity());

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(cartItem.getPrice());
            order.getOrderDetails().add(detail);

            // Decrease stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
    }

    private void validateProductAvailability(Product product, int quantity) {
        if (!product.isActive()) {
            throw new BusinessException("Product " + product.getName() + " is not available.");
        }
        if (product.getStock() < quantity) {
            throw new BusinessException("Insufficient stock for product " + product.getName() + ".");
        }
    }
}
