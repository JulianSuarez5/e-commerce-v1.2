package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product saveProduct(Product product);

    Optional<ProductDto> getProductById(Integer id);

    Product updateProduct(Product product);

    void deleteProduct(Integer id);

    List<ProductDto> findAll();

    List<ProductDto> findActiveProducts();

    Long countProducts();

    List<ProductDto> findByCategory(Integer categoryId);

    List<ProductDto> findByBrand(Integer brandId);

    List<ProductDto> searchProducts(String query);

    List<ProductDto> findTopSellingProducts(int limit);

    List<ProductDto> filterProducts(Integer categoryId, Integer brandId, Double minPrice, Double maxPrice, String query);
}
