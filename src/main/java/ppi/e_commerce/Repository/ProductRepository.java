package ppi.e_commerce.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    String PRODUCT_DTO_MAPPING = "new ppi.e_commerce.Dto.ProductDto(" +
            "p.id, p.name, p.description, p.price, p.stock, p.active, p.sku, " +
            "c.id, c.name, b.id, b.name, s.id, s.businessName)";

    @Query("SELECT " + PRODUCT_DTO_MAPPING + " FROM Product p " +
           "JOIN p.category c JOIN p.brand b JOIN p.seller s " +
           "WHERE (p.active = true) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId) " +
           "AND (:brandId IS NULL OR b.id = :brandId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<ProductDto> findProductsByCriteria(
            @Param("categoryId") Integer categoryId,
            @Param("brandId") Integer brandId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("query") String query,
            Pageable pageable);

    @Query("SELECT " + PRODUCT_DTO_MAPPING + " FROM Product p " +
           "JOIN p.category c JOIN p.brand b JOIN p.seller s " +
           "WHERE p.id = :id")
    ProductDto findProductDtoById(@Param("id") Integer id);

    @Query("SELECT " + PRODUCT_DTO_MAPPING + " FROM Product p " +
           "JOIN p.category c JOIN p.brand b JOIN p.seller s " +
           "WHERE s.id = :sellerId")
    Page<ProductDto> findProductsBySellerId(@Param("sellerId") Integer sellerId, Pageable pageable);

    List<Product> findByActiveTrue();

    Long countBySellerId(Integer sellerId);

}
