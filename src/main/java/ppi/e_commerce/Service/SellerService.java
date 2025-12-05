package ppi.e_commerce.Service;

import ppi.e_commerce.Model.Seller;
import java.util.List;
import java.util.Optional;

public interface SellerService {
    Seller createSeller(Integer userId, Seller seller);
    Optional<Seller> findById(Integer id);
    Optional<Seller> findByUserId(Integer userId);
    Optional<Seller> findByUsername(String username);
    List<Seller> findAllActive();
    List<Seller> findAllVerified();
    Seller updateSeller(Seller seller);
    void deleteSeller(Integer id);
    void updateSellerStats(Integer sellerId);
    Long countProductsBySeller(Integer sellerId);
}

