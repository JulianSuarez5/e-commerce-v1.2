package ppi.e_commerce.Controller.Api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Dto.OrderTrackingDto;
import ppi.e_commerce.Dto.UpdateOrderStatusRequest;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    private String getUsername(Authentication authentication) {
        if (authentication == null) {
            throw new AccessDeniedException("Authentication is required.");
        }
        return authentication.getName();
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(Authentication authentication) {
        String username = getUsername(authentication);
        List<OrderDto> orders = orderService.findUserOrders(username);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Integer id, Authentication authentication) {
        String username = getUsername(authentication);
        return orderService.findOrderById(id, username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    @GetMapping("/{id}/tracking")
    public ResponseEntity<List<OrderTrackingDto>> getOrderTracking(@PathVariable Integer id) {
        List<OrderTrackingDto> tracking = orderService.findOrderTracking(id);
        return ResponseEntity.ok(tracking);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Integer id,
            @RequestBody UpdateOrderStatusRequest request,
            Authentication authentication) {
        String username = getUsername(authentication);
        OrderDto updatedOrder = orderService.updateOrderStatusAndTrack(id, request, username);
        return ResponseEntity.ok(updatedOrder);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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
