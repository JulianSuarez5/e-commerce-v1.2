package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Dto.SellerDto;
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
    List<Seller> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);

    @Query("SELECT new ppi.e_commerce.Dto.SellerDto(s.id, s.businessName, s.description, s.website, s.taxId, s.phone, s.address, s.city, s.state, s.zipCode, s.country, s.logoUrl, s.rating, s.verified, s.active, s.createdAt, u.id, u.username, COUNT(p.id)) " +
           "FROM Seller s JOIN s.user u LEFT JOIN s.products p " +
           "WHERE s.active = :active AND (:verified IS NULL OR s.verified = :verified) " +
           "GROUP BY s.id, u.id")
    List<SellerDto> findAllSellersWithProductCount(@Param("active") boolean active, @Param("verified") Boolean verified);

    @Query("SELECT new ppi.e_commerce.Dto.SellerDto(s.id, s.businessName, s.description, s.website, s.taxId, s.phone, s.address, s.city, s.state, s.zipCode, s.country, s.logoUrl, s.rating, s.verified, s.active, s.createdAt, u.id, u.username, COUNT(p.id)) " +
           "FROM Seller s JOIN s.user u LEFT JOIN s.products p WHERE s.id = :id " +
           "GROUP BY s.id, u.id")
    Optional<SellerDto> findSellerDtoById(@Param("id") Integer id);

    @Query("SELECT new ppi.e_commerce.Dto.SellerDto(s.id, s.businessName, s.description, s.website, s.taxId, s.phone, s.address, s.city, s.state, s.zipCode, s.country, s.logoUrl, s.rating, s.verified, s.active, s.createdAt, u.id, u.username, COUNT(p.id)) " +
           "FROM Seller s JOIN s.user u LEFT JOIN s.products p WHERE u.id = :userId " +
           "GROUP BY s.id, u.id")
    Optional<SellerDto> findSellerDtoByUserId(@Param("userId") Integer userId);

}
