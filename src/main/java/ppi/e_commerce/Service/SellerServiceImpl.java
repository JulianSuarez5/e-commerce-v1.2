package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Exception.BusinessException;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Seller;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.SellerRepository;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Seller createSeller(Integer userId, Seller seller) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // Verificar si el usuario ya es vendedor
        if (sellerRepository.findByUserId(userId).isPresent()) {
            throw new BusinessException("El usuario ya es vendedor");
        }

        // Asignar rol SELLER al usuario
        String currentRole = user.getRole();
        if (currentRole == null || currentRole.isBlank() || currentRole.equals("USER")) {
            user.setRole("SELLER");
            userRepository.save(user);
        } else if (!currentRole.equals("SELLER") && !currentRole.equals("ADMIN")) {
            // Si tiene otro rol, agregar SELLER también (puede ser ADMIN y SELLER)
            user.setRole(currentRole + ",SELLER");
            userRepository.save(user);
        }

        seller.setUser(user);
        return sellerRepository.save(seller);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seller> findById(Integer id) {
        return sellerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seller> findByUserId(Integer userId) {
        return sellerRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seller> findByUsername(String username) {
        return sellerRepository.findByUserUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seller> findAllActive() {
        return sellerRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seller> findAllVerified() {
        return sellerRepository.findByVerifiedTrue();
    }

    @Override
    public Seller updateSeller(Seller seller) {
        if (!sellerRepository.existsById(seller.getId())) {
            throw new ResourceNotFoundException("Vendedor no encontrado con ID: " + seller.getId());
        }
        return sellerRepository.save(seller);
    }

    @Override
    public void deleteSeller(Integer id) {
        if (!sellerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vendedor no encontrado con ID: " + id);
        }
        sellerRepository.deleteById(id);
    }

    @Override
    public void updateSellerStats(Integer sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado"));

        // Calcular estadísticas desde productos y órdenes
        Long productCount = productRepository.countBySellerId(sellerId);
        // Calcular rating desde reviews, sales desde orders

        sellerRepository.save(seller);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countProductsBySeller(Integer sellerId) {
        return productRepository.countBySellerId(sellerId);
    }
}
