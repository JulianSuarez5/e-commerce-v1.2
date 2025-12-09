package ppi.e_commerce.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Dto.ProductVariantDto;

import java.util.List;

public interface ProductService {

    Page<ProductDto> findProducts(Integer categoryId, Integer brandId, Double minPrice, Double maxPrice, String query, Pageable pageable);

    ProductDto findById(Integer id);

    Page<ProductDto> findProductsBySeller(Integer sellerId, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(Integer productId, ProductDto productDto);

    void deleteProduct(Integer productId);

    boolean isOwner(Integer productId, String username);

    void saveProductImage(Integer productId, String fileName);

    void saveProductModel3D(Integer productId, String fileName);

    ProductVariantDto createProductVariant(Integer productId, ProductVariantDto variantDto);

    List<ProductVariantDto> getProductVariants(Integer productId, boolean activeOnly);

    ProductVariantDto getProductVariant(Integer productId, Integer variantId);

    ProductVariantDto updateProductVariant(Integer productId, Integer variantId, ProductVariantDto variantDto);

    void deleteProductVariant(Integer productId, Integer variantId);
}
