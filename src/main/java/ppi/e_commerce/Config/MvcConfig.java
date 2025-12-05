package ppi.e_commerce.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import java.nio.file.Paths;

@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir:./uploads/}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String resolvedUploadPath = Paths.get(uploadDir).toAbsolutePath().toUri().toString();
        log.info("Mapeando la ruta de recursos /uploads/** al directorio físico: {}", resolvedUploadPath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resolvedUploadPath);
    }
}
