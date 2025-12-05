package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Model.ProductModel3D;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductModel3DRepository extends JpaRepository<ProductModel3D, Integer> {
    List<ProductModel3D> findByProductId(Integer productId);
    Optional<ProductModel3D> findByProductIdAndIsPrimaryTrue(Integer productId);
}

