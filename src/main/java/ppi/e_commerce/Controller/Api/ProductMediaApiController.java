package ppi.e_commerce.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.ProductImage;
import ppi.e_commerce.Model.ProductModel3D;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.ProductImageRepository;
import ppi.e_commerce.Repository.ProductModel3DRepository;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Service.FileStorageService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products/{productId}")
public class ProductMediaApiController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductModel3DRepository productModel3DRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    private void validateOwnership(Product product, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Permitir si es Admin
        if (currentUser.getRole().equals("ADMIN")) {
            return;
        }

        // Verificar si el producto tiene un vendedor y si el usuario actual es ese vendedor
        if (product.getSeller() == null || product.getSeller().getUser() == null || !product.getSeller().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para modificar este producto.");
        }
    }

    @PostMapping("/images")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<?> uploadImage(
            @PathVariable Integer productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", defaultValue = "false") Boolean isPrimary,
            @RequestParam(value = "displayOrder", defaultValue = "0") Integer displayOrder,
            Authentication authentication) {
        try {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            validateOwnership(product, authentication);

            String imageUrl = fileStorageService.storeImage(file, "products");
            String thumbnailUrl = fileStorageService.generateThumbnail(imageUrl);

            if (isPrimary || productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId).isEmpty()) {
                productImageRepository.findByProductIdAndIsPrimaryTrue(productId)
                    .ifPresent(img -> {
                        img.setIsPrimary(false);
                        productImageRepository.save(img);
                    });
                isPrimary = true;
            }

            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setUrl(imageUrl);
            productImage.setThumbnailUrl(thumbnailUrl);
            productImage.setIsPrimary(isPrimary);
            productImage.setDisplayOrder(displayOrder);

            ProductImage saved = productImageRepository.save(productImage);

            Map<String, Object> response = new HashMap<>();
            response.put("id", saved.getId());
            response.put("url", saved.getUrl());
            response.put("thumbnailUrl", saved.getThumbnailUrl());
            response.put("isPrimary", saved.getIsPrimary());
            response.put("displayOrder", saved.getDisplayOrder());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar imagen: " + e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/images")
    public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Integer productId) {
        if (!productRepository.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }

        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<?> deleteImage(
            @PathVariable Integer productId,
            @PathVariable Integer imageId, Authentication authentication) {
        try {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            validateOwnership(product, authentication);

            ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));

            if (!image.getProduct().getId().equals(productId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La imagen no pertenece a este producto");
            }

            fileStorageService.deleteFile(image.getUrl());
            if (image.getThumbnailUrl() != null) {
                fileStorageService.deleteFile(image.getThumbnailUrl());
            }

            productImageRepository.delete(image);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar imagen: " + e.getMessage());
        }
    }

    @PostMapping("/models3d")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<?> uploadModel3D(
            @PathVariable Integer productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", defaultValue = "true") Boolean isPrimary,
            Authentication authentication) {
        try {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            validateOwnership(product, authentication);

            String originalFilename = file.getOriginalFilename();
            String format = originalFilename != null && originalFilename.endsWith(".gltf") 
                ? "GLTF" 
                : "GLB";

            String modelUrl = fileStorageService.storeModel3D(file, "products");

            if (isPrimary) {
                productModel3DRepository.findByProductIdAndIsPrimaryTrue(productId)
                    .ifPresent(model -> {
                        model.setIsPrimary(false);
                        productModel3DRepository.save(model);
                    });
            }

            ProductModel3D model3D = new ProductModel3D();
            model3D.setProduct(product);
            model3D.setUrl(modelUrl);
            model3D.setFormat(format);
            model3D.setFileSize(file.getSize());
            model3D.setIsPrimary(isPrimary);
            model3D.setOptimized(false);

            ProductModel3D saved = productModel3DRepository.save(model3D);

            if (isPrimary) {
                product.setModel3dUrl(modelUrl);
                productRepository.save(product);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", saved.getId());
            response.put("url", saved.getUrl());
            response.put("format", saved.getFormat());
            response.put("fileSize", saved.getFileSize());
            response.put("isPrimary", saved.getIsPrimary());
            response.put("optimized", saved.getOptimized());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar modelo 3D: " + e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/models3d")
    public ResponseEntity<List<ProductModel3D>> getProductModels3D(@PathVariable Integer productId) {
        if (!productRepository.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }

        List<ProductModel3D> models = productModel3DRepository.findByProductId(productId);
        return ResponseEntity.ok(models);
    }

    @DeleteMapping("/models3d/{modelId}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<?> deleteModel3D(
            @PathVariable Integer productId,
            @PathVariable Integer modelId, Authentication authentication) {
        try {
             Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            validateOwnership(product, authentication);

            ProductModel3D model = productModel3DRepository.findById(modelId)
                .orElseThrow(() -> new ResourceNotFoundException("Modelo 3D no encontrado"));

            if (!model.getProduct().getId().equals(productId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El modelo no pertenece a este producto");
            }

            fileStorageService.deleteFile(model.getUrl());
            productModel3DRepository.delete(model);

            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar modelo 3D: " + e.getMessage());
        }
    }
}
