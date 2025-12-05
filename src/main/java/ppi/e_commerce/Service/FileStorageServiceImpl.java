package ppi.e_commerce.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_MODEL_SIZE = 50 * 1024 * 1024; // 50MB
    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/webp"};
    private static final String[] ALLOWED_MODEL_TYPES = {"model/gltf-binary", "model/gltf+json", "application/octet-stream"};

    @Override
    public String storeImage(MultipartFile file, String folder) throws IOException {
        if (!validateImageFile(file)) {
            throw new IllegalArgumentException("Archivo de imagen inválido");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir, folder, "images");
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String relativePath = "/uploads/" + folder + "/images/" + fileName;
        log.info("Imagen guardada: {}", relativePath);
        return relativePath;
    }

    @Override
    public String storeModel3D(MultipartFile file, String folder) throws IOException {
        if (!validateModel3DFile(file)) {
            throw new IllegalArgumentException("Archivo 3D inválido");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : ".glb";
        
        String fileName = UUID.randomUUID().toString() + extension;
        Path uploadPath = Paths.get(uploadDir, folder, "models3d");
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String relativePath = "/uploads/" + folder + "/models3d/" + fileName;
        log.info("Modelo 3D guardado: {}", relativePath);
        return relativePath;
    }

    @Override
    public String generateThumbnail(String imageUrl) throws IOException {
        // TODO: Implementar generación de thumbnails usando ImageIO o similar
        // Por ahora retornar la misma URL
        return imageUrl;
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/uploads/")) {
                Path filePath = Paths.get(uploadDir, fileUrl.substring("/uploads/".length()));
                Files.deleteIfExists(filePath);
                log.info("Archivo eliminado: {}", fileUrl);
            }
        } catch (IOException e) {
            log.error("Error al eliminar archivo: {}", fileUrl, e);
        }
    }

    @Override
    public boolean validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        for (String allowedType : ALLOWED_IMAGE_TYPES) {
            if (contentType.equals(allowedType)) {
                return file.getSize() <= MAX_IMAGE_SIZE;
            }
        }

        return false;
    }

    @Override
    public boolean validateModel3DFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        
        if (originalFilename == null) {
            return false;
        }

        // Validar por extensión
        boolean validExtension = originalFilename.toLowerCase().endsWith(".glb") || 
                                 originalFilename.toLowerCase().endsWith(".gltf");

        // Validar por content type
        boolean validContentType = contentType != null && (
            contentType.equals("model/gltf-binary") ||
            contentType.equals("model/gltf+json") ||
            contentType.equals("application/octet-stream")
        );

        return (validExtension || validContentType) && file.getSize() <= MAX_MODEL_SIZE;
    }
}

