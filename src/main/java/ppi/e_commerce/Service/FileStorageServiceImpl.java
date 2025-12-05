package ppi.e_commerce.Service;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final Path fileStorageLocation;
    private final Tika tika = new Tika();

    @Value("${app.security.file.max-image-size-mb:10}")
    private long maxImageSize;

    @Value("${app.security.file.max-model-size-mb:50}")
    private long maxModelSize;

    @Value("${app.security.file.allowed-image-types}")
    private List<String> allowedImageTypes;

    @Value("${app.security.file.allowed-model-types}")
    private List<String> allowedModelTypes;

    public FileStorageServiceImpl(@Value("${app.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar los archivos subidos.", ex);
        }
    }

    private void validateFile(MultipartFile file, long maxSizeInMB, List<String> allowedTypes) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío.");
        }

        long maxSizeInBytes = maxSizeInMB * 1024 * 1024;
        if (file.getSize() > maxSizeInBytes) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo permitido de " + maxSizeInMB + "MB.");
        }

        String detectedType;
        try (InputStream inputStream = file.getInputStream()) {
            detectedType = tika.detect(inputStream, file.getOriginalFilename());
        }

        if (!allowedTypes.contains(detectedType)) {
            throw new IllegalArgumentException("Tipo de archivo no permitido. Detectado: " + detectedType + ", Permitidos: " + allowedTypes);
        }
    }

    private String store(MultipartFile file, String subfolder) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString() + extension;
        Path targetLocation = this.fileStorageLocation.resolve(subfolder);

        if (!Files.exists(targetLocation)) {
            Files.createDirectories(targetLocation);
        }

        Path filePath = targetLocation.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String relativePath = "/uploads/" + subfolder + "/" + fileName;
        log.info("Archivo guardado en: {}", relativePath);
        return relativePath;
    }

    @Override
    public String storeImage(MultipartFile file, String folder) throws IOException {
        validateFile(file, maxImageSize, allowedImageTypes);
        return store(file, "images/" + folder);
    }

    @Override
    public String storeModel3D(MultipartFile file, String folder) throws IOException {
        validateFile(file, maxModelSize, allowedModelTypes);
        return store(file, "models3d/" + folder);
    }

    @Override
    public String generateThumbnail(String imageUrl) throws IOException {
        // TODO: Implementar la generación de miniaturas (thumbnails).
        return imageUrl;
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/uploads/")) {
                String relativePath = fileUrl.substring("/uploads/".length());
                Path filePath = this.fileStorageLocation.resolve(relativePath).normalize();
                Files.deleteIfExists(filePath);
                log.info("Archivo eliminado: {}", fileUrl);
            }
        } catch (IOException e) {
            log.error("Error al eliminar el archivo: {}", fileUrl, e);
        }
    }
}
