package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Mapper.ProductMapper;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> getProductById(Integer id) {
        return productRepository.findById(id).map(productMapper::productToProductDto);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productMapper.productsToProductDtos(productRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findActiveProducts() {
        return productMapper.productsToProductDtos(productRepository.findByActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countProducts() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findByCategory(Integer categoryId) {
        return productMapper.productsToProductDtos(productRepository.findByCategoryId(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findByBrand(Integer brandId) {
        return productMapper.productsToProductDtos(productRepository.findByBrandId(brandId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String query) {
        return filterProducts(null, null, null, null, query);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findTopSellingProducts(int limit) {
        return productMapper.productsToProductDtos(productRepository.findTopSellingProducts(limit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> filterProducts(Integer categoryId, Integer brandId, Double minPrice, Double maxPrice, String query) {
        List<Product> products = productRepository.findAll((Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }
            
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            
            if (query != null && !query.isBlank()) {
                String queryLower = "%" + query.toLowerCase() + "%";
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), queryLower);
                Predicate descLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), queryLower);
                predicates.add(criteriaBuilder.or(nameLike, descLike));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return productMapper.productsToProductDtos(products);
    }
}
