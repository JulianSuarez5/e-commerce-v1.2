package ppi.e_commerce.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.ProductMapper;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Repository.ProductRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findProducts(Integer categoryId, Integer brandId, Double minPrice, Double maxPrice, String query, Pageable pageable) {
        log.info("Filtering products with criteria: categoryId={}, brandId={}, minPrice={}, maxPrice={}, query='{}', pageable={}",
                categoryId, brandId, minPrice, maxPrice, query, pageable);
        return productRepository.findProductsByCriteria(categoryId, brandId, minPrice, maxPrice, query, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findById(Integer id) {
        log.info("Fetching product by ID: {}", id);
        ProductDto productDto = productRepository.findProductDtoById(id);
        if (productDto == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return productDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findProductsBySeller(Integer sellerId, Pageable pageable) {
        log.info("Fetching products for seller ID: {} with pageable: {}", sellerId, pageable);
        return productRepository.findProductsBySellerId(sellerId, pageable);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Creating new product with name: {}", productDto.getName());
        Product product = new Product();
        productMapper.updateEntityFromDto(productDto, product);
        Product savedProduct = productRepository.save(product);
        log.info("Successfully created product with ID: {}", savedProduct.getId());
        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Integer productId, ProductDto productDto) {
        log.info("Updating product with ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        productMapper.updateEntityFromDto(productDto, product);
        Product updatedProduct = productRepository.save(product);
        log.info("Successfully updated product with ID: {}", updatedProduct.getId());
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer productId) {
        log.info("Deleting product with ID: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with ID: " + productId);
        }
        productRepository.deleteById(productId);
        log.info("Successfully deleted product with ID: {}", productId);
    }
}
