package ppi.e_commerce.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Dto.ProductVariantDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.ProductMapper;
import ppi.e_commerce.Mapper.ProductVariantMapper;
import ppi.e_commerce.Model.*;
import ppi.e_commerce.Repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductModel3DRepository productModel3DRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final ProductVariantMapper productVariantMapper;

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

    @Override
    public boolean isOwner(Integer productId, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        return product.getSeller().getUser().getUsername().equals(username);
    }

    @Override
    public void saveProductImage(Integer productId, String fileName) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        ProductImage image = new ProductImage(product, fileName);
        productImageRepository.save(image);
    }

    @Override
    public void saveProductModel3D(Integer productId, String fileName) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        ProductModel3D model3D = new ProductModel3D(product, fileName);
        productModel3DRepository.save(model3D);
    }

    @Override
    public ProductVariantDto createProductVariant(Integer productId, ProductVariantDto variantDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        ProductVariant variant = new ProductVariant();
        productVariantMapper.updateEntityFromDto(variantDto, variant);
        variant.setProduct(product);
        ProductVariant savedVariant = productVariantRepository.save(variant);
        return productVariantMapper.toDto(savedVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantDto> getProductVariants(Integer productId, boolean activeOnly) {
        List<ProductVariant> variants = activeOnly
                ? productVariantRepository.findByProductIdAndActiveTrue(productId)
                : productVariantRepository.findByProductId(productId);
        return variants.stream()
                .map(productVariantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantDto getProductVariant(Integer productId, Integer variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with ID: " + variantId));
        if (!variant.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Product variant does not belong to the specified product");
        }
        return productVariantMapper.toDto(variant);
    }

    @Override
    public ProductVariantDto updateProductVariant(Integer productId, Integer variantId, ProductVariantDto variantDto) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with ID: " + variantId));
        if (!variant.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Product variant does not belong to the specified product");
        }
        productVariantMapper.updateEntityFromDto(variantDto, variant);
        ProductVariant updatedVariant = productVariantRepository.save(variant);
        return productVariantMapper.toDto(updatedVariant);
    }

    @Override
    public void deleteProductVariant(Integer productId, Integer variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with ID: " + variantId));
        if (!variant.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Product variant does not belong to the specified product");
        }
        productVariantRepository.delete(variant);
    }
}
