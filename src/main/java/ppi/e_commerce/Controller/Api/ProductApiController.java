package ppi.e_commerce.Controller.Api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Service.ProductService;

@Slf4j
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("Request to filter products: categoryId={}, brandId={}, query='{}', minPrice={}, maxPrice={}, pageable={}",
                categoryId, brandId, q, minPrice, maxPrice, pageable);

        Page<ProductDto> productPage = productService.findProducts(categoryId, brandId, minPrice, maxPrice, q, pageable);
        log.info("Found {} products matching criteria across {} pages.", productPage.getTotalElements(), productPage.getTotalPages());

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        log.info("Request to fetch product with ID: {}", id);
        ProductDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
