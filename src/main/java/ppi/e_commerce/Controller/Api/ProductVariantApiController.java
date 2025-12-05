package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.ProductVariantDto;
import ppi.e_commerce.Service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/variants")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ProductVariantApiController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ProductVariantDto> createVariant(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductVariantDto variantDto) {
        ProductVariantDto createdVariant = productService.createProductVariant(productId, variantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVariant);
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantDto>> getVariants(
            @PathVariable Integer productId,
            @RequestParam(value = "activeOnly", defaultValue = "false") Boolean activeOnly) {
        List<ProductVariantDto> variants = productService.getProductVariants(productId, activeOnly);
        return ResponseEntity.ok(variants);
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ProductVariantDto> getVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId) {
        ProductVariantDto variant = productService.getProductVariant(productId, variantId);
        return ResponseEntity.ok(variant);
    }

    @PutMapping("/{variantId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ProductVariantDto> updateVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId,
            @Valid @RequestBody ProductVariantDto variantDto) {
        ProductVariantDto updatedVariant = productService.updateProductVariant(productId, variantId, variantDto);
        return ResponseEntity.ok(updatedVariant);
    }

    @DeleteMapping("/{variantId}")
    @PreAuthorize("hasRole('SELER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId) {
        productService.deleteProductVariant(productId, variantId);
        return ResponseEntity.ok().build();
    }
}
