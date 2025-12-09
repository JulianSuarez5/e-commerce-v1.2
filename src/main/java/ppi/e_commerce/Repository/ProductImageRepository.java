package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Model.ProductImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(Integer productId);
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Integer productId);
    void deleteByProductId(Integer productId);
}

