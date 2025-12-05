package ppi.e_commerce.Controller.Api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CategoryDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryApiController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        log.info("Request received to fetch all active categories.");
        List<CategoryDto> categories = categoryService.findActiveCategories();
        log.info("Found {} active categories.", categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {
        log.info("Request received to fetch category with ID: {}", id);
        CategoryDto category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
