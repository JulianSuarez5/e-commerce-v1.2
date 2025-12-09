package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CheckoutRequest;
import ppi.e_commerce.Dto.OrderDto;
import ppi.e_commerce.Exception.BusinessException;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Service.CheckoutService;

@Slf4j
@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class CheckoutApiController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<OrderDto> checkout(
            @Valid @RequestBody CheckoutRequest request,
            Authentication authentication) {

        if (authentication == null) {
            throw new AccessDeniedException("Authentication is required for checkout.");
        }

        String username = authentication.getName();
        log.info("Checkout process started for user: {}", username);

        OrderDto orderDto = checkoutService.processCheckout(username, request);

        log.info("Checkout successful for user: {}. Order number: {}", username, orderDto.getNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        log.warn("Business rule violation during checkout: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied during checkout: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedError(Exception ex) {
        log.error("An unexpected error occurred during checkout", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during checkout.");
    }
}
