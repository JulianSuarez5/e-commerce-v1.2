package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Model.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductIdAndActiveTrueOrderByCreatedAtDesc(Integer productId);
    List<Review> findByUserId(Integer userId);
    Optional<Review> findByProductIdAndUserId(Integer productId, Integer userId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId AND r.active = true")
    Double getAverageRatingByProductId(Integer productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.active = true")
    Long countByProductId(Integer productId);
}

