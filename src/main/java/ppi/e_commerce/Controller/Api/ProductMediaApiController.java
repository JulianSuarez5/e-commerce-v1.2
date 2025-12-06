package ppi.e_commerce.Controller.Api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Service.ProductService;
import ppi.e_commerce.Service.FileStorageService;
import ppi.e_commerce.Service.UserService;

@RestController
@RequestMapping("/api/products/{productId}/media")
@RequiredArgsConstructor
public class ProductMediaApiController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;
    private final UserService userService;

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Integer productId, @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) {
        try {
            // Validar que el usuario es el vendedor del producto
            if (!productService.isOwner(productId, user.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this product.");
            }

            // Store image under a folder per product to keep files organized
            String fileName = fileStorageService.storeImage(file, String.valueOf(productId));
            productService.saveProductImage(productId, fileName);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/upload-model3d")
    public ResponseEntity<?> uploadModel3D(@PathVariable Integer productId, @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) {
        try {
            // Validar que el usuario es el vendedor del producto
            if (!productService.isOwner(productId, user.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this product.");
            }

            // Store 3D model under a folder per product
            String fileName = fileStorageService.storeModel3D(file, String.valueOf(productId));
            productService.saveProductModel3D(productId, fileName);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
