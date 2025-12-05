package ppi.e_commerce.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    String storeImage(MultipartFile file, String folder) throws IOException;
    String storeModel3D(MultipartFile file, String folder) throws IOException;
    String generateThumbnail(String imageUrl) throws IOException;
    void deleteFile(String fileUrl);
    boolean validateImageFile(MultipartFile file);
    boolean validateModel3DFile(MultipartFile file);
}

