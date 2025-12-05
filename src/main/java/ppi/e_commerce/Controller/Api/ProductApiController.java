package ppi.e_commerce.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        
        List<ProductDto> productDtos = productService.filterProducts(categoryId, brandId, minPrice, maxPrice, q);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
