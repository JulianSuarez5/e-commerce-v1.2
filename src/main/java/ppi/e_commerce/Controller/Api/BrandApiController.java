package ppi.e_commerce.Controller.Api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.BrandDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Service.BrandService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BrandApiController {

    private final BrandService brandService;

    @Autowired
    public BrandApiController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        log.info("Request to get all active brands.");
        List<BrandDto> brands = brandService.findActiveBrands();
        log.debug("Found {} active brands.", brands.size());
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Integer id) {
        log.info("Request to get brand with ID: {}", id);
        BrandDto brand = brandService.findById(id);
        log.debug("Brand found: {}", brand);
        return ResponseEntity.ok(brand);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
