package ppi.e_commerce.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ppi.e_commerce.Model.*;
import ppi.e_commerce.Repository.*;

@Service
public class DataInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @org.springframework.beans.factory.annotation.Value("${app.seed.create-sample-data:false}")
    private boolean createSampleData;

    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }

    private void initializeData() {
        if (!createSampleData) {
            logger.info("✅ Seed data deshabilitado. Todos los datos vienen de la base de datos.");
            return; // No crear datos de ejemplo
        }

        logger.info("⚠️ Creando datos de ejemplo (app.seed.create-sample-data=true)...");

        // Crear categorías de ejemplo si no existen
        if (categoryRepository.count() == 0) {
            Category electronics = new Category("Electrónicos");
            Category clothing = new Category("Ropa");
            Category home = new Category("Hogar");
            Category sports = new Category("Deportes");

            categoryRepository.save(electronics);
            categoryRepository.save(clothing);
            categoryRepository.save(home);
            categoryRepository.save(sports);

            logger.info("✅ Categorías de ejemplo creadas");
        }

        // Crear marcas de ejemplo si no existen
        if (brandRepository.count() == 0) {
            Brand samsung = new Brand("Samsung");
            Brand apple = new Brand("Apple");
            Brand nike = new Brand("Nike");
            Brand adidas = new Brand("Adidas");

            brandRepository.save(samsung);
            brandRepository.save(apple);
            brandRepository.save(nike);
            brandRepository.save(adidas);

            logger.info("✅ Marcas de ejemplo creadas");
        }

        // Crear productos de ejemplo si no existen (CORREGIDO)
        if (productRepository.count() == 0) {
            var categories = categoryRepository.findAll();
            var brands = brandRepository.findAll();

            if (!categories.isEmpty() && !brands.isEmpty()) {
                Category electronics = categories.get(0);
                Brand samsung = brands.get(0);
                Brand apple = brands.size() > 1 ? brands.get(1) : samsung;

                // Use the Product constructor: (name, description, price, stock, sku, imageUrl, category, brand)
                Product product1 = new Product(
                        "Smartphone Galaxy S24",
                        "El último smartphone de Samsung con IA",
                        2000000.0,
                        10,
                        "GAL-S24",
                        "https://via.placeholder.com/300x200/3498db/ffffff?text=Galaxy+S24",
                        electronics,
                        samsung
                );

                Product product2 = new Product(
                        "iPhone 15 Pro",
                        "El iPhone más avanzado con chip A17 Pro",
                        4500000.0,
                        5,
                        "IP15-PRO",
                        "https://via.placeholder.com/300x200/e74c3c/ffffff?text=iPhone+15",
                        electronics,
                        apple
                );

                productRepository.save(product1);
                productRepository.save(product2);

                logger.info("✅ Productos de ejemplo creados");
            }
        }

        logger.info("✅ Inicialización de datos completada");
    }
}
