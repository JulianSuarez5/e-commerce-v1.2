package ppi.e_commerce.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.CreateSellerRequest;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Dto.SellerDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Exception.SellerAccessDeniedException;
import ppi.e_commerce.Exception.SellerAlreadyExistsException;
import ppi.e_commerce.Mapper.SellerMapper;
import ppi.e_commerce.Model.Seller;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Model.UserRole;
import ppi.e_commerce.Repository.SellerRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final SellerMapper sellerMapper;
    private final ProductService productService;

    @Override
    public SellerDto createSeller(String username, CreateSellerRequest request) {
        log.info("Attempting to create a seller for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (sellerRepository.findByUserId(user.getId()).isPresent()) {
            throw new SellerAlreadyExistsException("This user is already a seller.");
        }

        user.setRole(UserRole.SELLER); // Assign SELLER role
        userRepository.save(user);

        Seller seller = new Seller();
        seller.setUser(user);
        sellerMapper.updateEntityFromRequest(request, seller);

        Seller savedSeller = sellerRepository.save(seller);
        log.info("Successfully created seller with ID: {}", savedSeller.getId());

        return sellerMapper.toDto(savedSeller);
    }

    @Override
    public SellerDto updateSeller(Integer sellerId, String username, CreateSellerRequest request) {
        log.info("Attempting to update seller {} by user {}", sellerId, username);

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        if (!seller.getUser().getUsername().equals(username)) {
             throw new SellerAccessDeniedException("User does not have permission to update this seller.");
        }

        sellerMapper.updateEntityFromRequest(request, seller);
        Seller updatedSeller = sellerRepository.save(seller);
        log.info("Successfully updated seller with ID: {}", updatedSeller.getId());

        return sellerMapper.toDto(updatedSeller);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerDto> findSellers(boolean active, Boolean verified) {
        log.info("Fetching all sellers with active={} and verified={}", active, verified);
        return sellerRepository.findAllSellersWithProductCount(active, verified);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerDto findSellerById(Integer id) {
        log.info("Fetching seller by ID: {}", id);
        return sellerRepository.findSellerDtoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public SellerDto findSellerByUserId(Integer userId) {
        log.info("Fetching seller by user ID: {}", userId);
        return sellerRepository.findSellerDtoByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found for user ID: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findSellerProducts(Integer sellerId, Pageable pageable) {
        log.info("Fetching products for seller ID: {} with pageable: {}", sellerId, pageable);
        if (!sellerRepository.existsById(sellerId)) {
            throw new ResourceNotFoundException("Seller not found with ID: " + sellerId);
        }
        return productService.findProductsBySeller(sellerId, pageable);
    }

    @Override
    public void deleteSeller(Integer sellerId) {
        log.info("Deleting seller with ID: {}", sellerId);
        if (!sellerRepository.existsById(sellerId)) {
            throw new ResourceNotFoundException("Seller not found with ID: " + sellerId);
        }
        sellerRepository.deleteById(sellerId);
        log.info("Successfully deleted seller with ID: {}", sellerId);
    }

}
