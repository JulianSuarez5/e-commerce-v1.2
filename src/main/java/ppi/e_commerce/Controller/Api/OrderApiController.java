package ppi.e_commerce.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Order;
import ppi.e_commerce.Model.OrderTracking;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.OrderRepository;
import ppi.e_commerce.Repository.OrderTrackingRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderApiController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTrackingRepository trackingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Order> orders = orderRepository.findByUserOrderByCreationDateDesc(user);
        List<OrderDto> dtos = orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable Integer id,
            Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        String username = authentication.getName();
        if (!order.getUser().getUsername().equals(username)
                && !authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(toDto(order));
    }

    @GetMapping("/{id}/tracking")
    public ResponseEntity<List<OrderTrackingDto>> getOrderTracking(@PathVariable Integer id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<OrderTracking> tracking = trackingRepository.findByOrderIdOrderByTimestampAsc(id);
        List<OrderTrackingDto> dtos = tracking.stream()
                .map(t -> {
                    OrderTrackingDto dto = new OrderTrackingDto();
                    dto.setId(t.getId());
                    dto.setStatus(t.getStatus());
                    dto.setDescription(t.getDescription());
                    dto.setLocation(t.getLocation());
                    dto.setTrackingNumber(t.getTrackingNumber());
                    dto.setTimestamp(t.getTimestamp());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer id,
            @RequestBody java.util.Map<String, String> request,
            Authentication authentication) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

            // Solo admin o seller del producto puede actualizar
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin) {
                return ResponseEntity.status(403).build();
            }

            String newStatus = request.get("status");
            order.setStatus(newStatus);

            // Crear tracking entry
            OrderTracking tracking = new OrderTracking();
            tracking.setOrder(order);
            tracking.setStatus(newStatus);
            tracking.setDescription(request.get("description"));
            tracking.setLocation(request.get("location"));
            tracking.setTrackingNumber(request.get("trackingNumber"));
            trackingRepository.save(tracking);

            orderRepository.save(order);
            return ResponseEntity.ok(toDto(order));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar estado: " + e.getMessage());
        }
    }

    private OrderDto toDto(Order order) {
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
        dto.setShippedDate(order.getShippedDate());
        dto.setReceiveDate(order.getReceiveDate());

        if (order.getOrderDetails() != null) {
            dto.setOrderDetails(order.getOrderDetails().stream()
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
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static class OrderTrackingDto {

        private Integer id;
        private String status;
        private String description;
        private String location;
        private String trackingNumber;
        private java.time.LocalDateTime timestamp;

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public java.time.LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(java.time.LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    private static class Map {

        public static java.util.Map<String, String> of(String... pairs) {
            java.util.Map<String, String> map = new java.util.HashMap<>();
            for (int i = 0; i < pairs.length; i += 2) {
                map.put(pairs[i], pairs[i + 1]);
            }
            return map;
        }
    }
}
