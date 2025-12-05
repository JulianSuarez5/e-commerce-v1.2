package ppi.e_commerce.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ppi.e_commerce.Dto.ProductDto;

public interface ProductService {

    Page<ProductDto> findProducts(Integer categoryId, Integer brandId, Double minPrice, Double maxPrice, String query, Pageable pageable);

    ProductDto findById(Integer id);

    Page<ProductDto> findProductsBySeller(Integer sellerId, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(Integer productId, ProductDto productDto);

    void deleteProduct(Integer productId);
}
