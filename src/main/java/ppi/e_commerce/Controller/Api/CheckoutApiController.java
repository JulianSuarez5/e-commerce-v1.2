package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CheckoutRequest;
import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.*;
// import ppi.e_commerce.Repository.CartRepository;
import ppi.e_commerce.Repository.OrderRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Service.CartService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CheckoutApiController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    // @Autowired
    // private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request,
            Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            Cart cart = cartService.getOrCreateCart(user);
            List<CartItem> items = cart.getCartItems();

            if (items == null || items.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El carrito está vacío");
            }

            // Validar stock y calcular total
            double total = 0.0;
            for (CartItem item : items) {
                Product product = item.getProduct();
                if (!product.isActive() || product.getCantidad() < item.getQuantity()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Producto " + product.getName() + " no disponible o stock insuficiente");
                }
                total += item.getPrice() * item.getQuantity();
            }

            // Crear orden
            String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Order order = new Order();
            order.setNumber(orderNumber);
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

            // Crear detalles de orden
            for (CartItem cartItem : items) {
                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(cartItem.getProduct());
                detail.setQuantity(cartItem.getQuantity());
                detail.setPrice(cartItem.getPrice());
                order.getOrderDetails().add(detail);

                // Reducir stock
                Product product = cartItem.getProduct();
                product.setCantidad(product.getCantidad() - cartItem.getQuantity());
                productRepository.save(product);
            }

            Order savedOrder = orderRepository.save(order);

            // Limpiar carrito
            cartService.clearCart(user);

            // Crear pago simulado
            Payment payment = new Payment();
            payment.setOrder(savedOrder);
            payment.setAmount(total);
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setStatus("PENDING");
            payment.setPaymentDate(LocalDateTime.now());
            // paymentRepository.save(payment); // Si existe PaymentRepository

            OrderDto orderDto = toOrderDto(savedOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en checkout: " + e.getMessage());
        }
    }

    private OrderDto toOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setNumber(order.getNumber());
        dto.setUserId(order.getUser().getId());
        dto.setUsername(order.getUser().getUsername());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingState(order.getShippingState());
        dto.setShippingZipCode(order.getShippingZipCode());
        dto.setNotes(order.getNotes());
        dto.setCreationDate(order.getCreationDate());

        // Convertir detalles
        if (order.getOrderDetails() != null) {
            List<ppi.e_commerce.Dto.OrderDetailDto> detailDtos = order.getOrderDetails().stream()
                    .map(detail -> {
                        ppi.e_commerce.Dto.OrderDetailDto detailDto = new ppi.e_commerce.Dto.OrderDetailDto();
                        detailDto.setId(detail.getId());
                        detailDto.setOrderId(detail.getOrder().getId());
                        detailDto.setProductId(detail.getProduct().getId());
                        detailDto.setProductName(detail.getProduct().getName());
                        detailDto.setProductImageUrl(detail.getProduct().getImageUrl());
                        detailDto.setPrice(detail.getPrice());
                        detailDto.setQuantity(detail.getQuantity());
                        detailDto.setSubtotal(detail.getPrice() * detail.getQuantity());
                        return detailDto;
                    })
                    .collect(Collectors.toList());
            dto.setOrderDetails(detailDtos);
        }

        return dto;
    }
}
