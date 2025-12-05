package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Model.Seller;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
    Optional<Seller> findByUserId(Integer userId);
    Optional<Seller> findByUserUsername(String username);
    List<Seller> findByActiveTrue();
    List<Seller> findByVerifiedTrue();
    
    @Query("SELECT s FROM Seller s WHERE s.rating >= :minRating")
    List<Seller> findByRatingGreaterThanEqual(Double minRating);
}

