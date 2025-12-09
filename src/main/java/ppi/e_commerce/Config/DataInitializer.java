package ppi.e_commerce.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DataInitializer {

    @Value("${app.seed.create-default-admin:false}")
    private boolean createDefaultAdmin;

    @Bean
    public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!createDefaultAdmin) {
                log.info("Skipping default admin user creation as per configuration.");
                return;
            }

            String adminUsername = "admin";
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setName("Administrador");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
                log.info("Default admin user created: {} / admin123", adminUsername);
            } else {
                log.info("Default admin user already exists. Skipping creation.");
            }
        };
    }
}
