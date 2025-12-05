package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Exception.BusinessException;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.Review;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.ReviewRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Review createReview(Integer productId, Integer userId, Review review) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Verificar si el usuario ya hizo una review
        if (reviewRepository.findByProductIdAndUserId(productId, userId).isPresent()) {
            throw new BusinessException("Ya has realizado una reseña para este producto");
        }

        review.setProduct(product);
        review.setUser(user);
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getProductReviews(Integer productId) {
        return reviewRepository.findByProductIdAndActiveTrueOrderByCreatedAtDesc(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Review updateReview(Review review) {
        if (!reviewRepository.existsById(review.getId())) {
            throw new ResourceNotFoundException("Reseña no encontrada");
        }
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reseña no encontrada");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(Integer productId) {
        Double avg = reviewRepository.getAverageRatingByProductId(productId);
        return avg != null ? avg : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getReviewCount(Integer productId) {
        return reviewRepository.countByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReviewed(Integer productId, Integer userId) {
        return reviewRepository.findByProductIdAndUserId(productId, userId).isPresent();
    }
}

