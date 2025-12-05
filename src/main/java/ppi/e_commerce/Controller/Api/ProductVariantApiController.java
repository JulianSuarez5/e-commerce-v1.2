package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.ProductVariantDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.ProductVariant;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.ProductVariantRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/variants")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductVariantApiController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<?> createVariant(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductVariantDto variantDto) {
        try {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setName(variantDto.getName());
            variant.setSku(variantDto.getSku());
            variant.setColor(variantDto.getColor());
            variant.setSize(variantDto.getSize());
            variant.setMaterial(variantDto.getMaterial());
            variant.setPriceModifier(variantDto.getPriceModifier());
            variant.setStock(variantDto.getStock());
            variant.setImageUrl(variantDto.getImageUrl());
            variant.setActive(variantDto.getActive() != null ? variantDto.getActive() : true);

            ProductVariant saved = variantRepository.save(variant);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al crear variante: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantDto>> getVariants(
            @PathVariable Integer productId,
            @RequestParam(value = "activeOnly", defaultValue = "false") Boolean activeOnly) {
        
        if (!productRepository.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }

        List<ProductVariant> variants = activeOnly
            ? variantRepository.findByProductIdAndActiveTrue(productId)
            : variantRepository.findByProductId(productId);

        List<ProductVariantDto> dtos = variants.stream()
            .map(this::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ProductVariantDto> getVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId) {
        
        ProductVariant variant = variantRepository.findById(variantId)
            .orElse(null);

        if (variant == null || !variant.getProduct().getId().equals(productId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toDto(variant));
    }

    @PutMapping("/{variantId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId,
            @Valid @RequestBody ProductVariantDto variantDto) {
        try {
            ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada"));

            if (!variant.getProduct().getId().equals(productId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La variante no pertenece a este producto");
            }

            variant.setName(variantDto.getName());
            variant.setSku(variantDto.getSku());
            variant.setColor(variantDto.getColor());
            variant.setSize(variantDto.getSize());
            variant.setMaterial(variantDto.getMaterial());
            variant.setPriceModifier(variantDto.getPriceModifier());
            variant.setStock(variantDto.getStock());
            variant.setImageUrl(variantDto.getImageUrl());
            if (variantDto.getActive() != null) {
                variant.setActive(variantDto.getActive());
            }

            ProductVariant updated = variantRepository.save(variant);
            return ResponseEntity.ok(toDto(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al actualizar variante: " + e.getMessage());
        }
    }

    @DeleteMapping("/{variantId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteVariant(
            @PathVariable Integer productId,
            @PathVariable Integer variantId) {
        try {
            ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada"));

            if (!variant.getProduct().getId().equals(productId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La variante no pertenece a este producto");
            }

            variantRepository.delete(variant);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar variante: " + e.getMessage());
        }
    }

    private ProductVariantDto toDto(ProductVariant variant) {
        ProductVariantDto dto = new ProductVariantDto();
        dto.setId(variant.getId());
        dto.setProductId(variant.getProduct().getId());
        dto.setName(variant.getName());
        dto.setSku(variant.getSku());
        dto.setColor(variant.getColor());
        dto.setSize(variant.getSize());
        dto.setMaterial(variant.getMaterial());
        dto.setPriceModifier(variant.getPriceModifier());
        dto.setStock(variant.getStock());
        dto.setImageUrl(variant.getImageUrl());
        dto.setActive(variant.getActive());
        return dto;
    }
}

