package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CreateSellerRequest;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Dto.SellerDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Exception.SellerAccessDeniedException;
import ppi.e_commerce.Exception.SellerAlreadyExistsException;
import ppi.e_commerce.Service.SellerService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sellers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SellerApiController {

    private final SellerService sellerService;

    public SellerApiController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SellerDto> createSeller(
            @Valid @RequestBody CreateSellerRequest request,
            Authentication authentication) {
        log.info("POST /api/sellers invoked by user: {}", authentication.getName());
        SellerDto createdSeller = sellerService.createSeller(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
    }

    @PutMapping("/{sellerId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<SellerDto> updateSeller(
            @PathVariable Integer sellerId,
            @Valid @RequestBody CreateSellerRequest request,
            Authentication authentication) {
        log.info("PUT /api/sellers/{} invoked by user: {}", sellerId, authentication.getName());
        SellerDto updatedSeller = sellerService.updateSeller(sellerId, authentication.getName(), request);
        return ResponseEntity.ok(updatedSeller);
    }

    @GetMapping
    public ResponseEntity<List<SellerDto>> findSellers(
            @RequestParam(defaultValue = "true") boolean active,
            @RequestParam(required = false) Boolean verified) {
        log.info("GET /api/sellers invoked with active={}, verified={}", active, verified);
        List<SellerDto> sellers = sellerService.findSellers(active, verified);
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerDto> getSellerById(@PathVariable Integer sellerId) {
        log.info("GET /api/sellers/{} invoked", sellerId);
        SellerDto seller = sellerService.findSellerById(sellerId);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SellerDto> getSellerByUserId(@PathVariable Integer userId) {
        log.info("GET /api/sellers/user/{} invoked", userId);
        SellerDto seller = sellerService.findSellerByUserId(userId);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/{sellerId}/products")
    public ResponseEntity<Page<ProductDto>> getSellerProducts(
            @PathVariable Integer sellerId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/sellers/{}/products invoked with pageable: {}", sellerId, pageable);
        Page<ProductDto> products = sellerService.findSellerProducts(sellerId, pageable);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{sellerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSeller(@PathVariable Integer sellerId) {
        log.info("DELETE /api/sellers/{} invoked", sellerId);
        sellerService.deleteSeller(sellerId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SellerAlreadyExistsException.class)
    public ResponseEntity<String> handleSellerAlreadyExists(SellerAlreadyExistsException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(SellerAccessDeniedException.class)
    public ResponseEntity<String> handleSellerAccessDenied(SellerAccessDeniedException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
