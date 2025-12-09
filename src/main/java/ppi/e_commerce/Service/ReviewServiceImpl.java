package ppi.e_commerce.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.CreateReviewRequest;
import ppi.e_commerce.Dto.ReviewDto;
import ppi.e_commerce.Dto.ReviewStatsDto;
import ppi.e_commerce.Exception.BusinessException;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.ReviewMapper;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.Review;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.OrderRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.ReviewRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository,
                           OrderRepository orderRepository,
                           ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewDto createReview(Integer productId, String username, CreateReviewRequest request) {
        log.info("Attempting to create a review for product {} by user {}", productId, username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (reviewRepository.findByProductIdAndUserId(productId, user.getId()).isPresent()) {
            throw new BusinessException("User has already submitted a review for this product.");
        }

        boolean isVerifiedPurchase = orderRepository.existsByUserAndProductInOrder(user.getId(), productId);
        log.debug("User {} purchase status for product {}: {}", username, productId, isVerifiedPurchase ? "Verified" : "Not Verified");

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());
        review.setVerifiedPurchase(isVerifiedPurchase);
        review.setActive(true); // Default to active

        Review savedReview = reviewRepository.save(review);
        log.info("Successfully created review with ID {} for product {}", savedReview.getId(), productId);

        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getProductReviews(Integer productId) {
        log.info("Fetching reviews for product ID: {}", productId);
        List<Review> reviews = reviewRepository.findByProductIdAndActiveTrueOrderByCreatedAtDesc(productId);
        return reviewMapper.toDtoList(reviews);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewStatsDto getReviewStats(Integer productId) {
        log.info("Fetching review statistics for product ID: {}", productId);
        Double averageRating = reviewRepository.getAverageRatingByProductId(productId);
        Long reviewCount = reviewRepository.countByProductId(productId);
        return new ReviewStatsDto(averageRating != null ? averageRating : 0.0, reviewCount);
    }

    @Override
    public void deleteReview(Integer reviewId) {
        log.info("Deleting review with ID: {}", reviewId);
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with ID: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
        log.info("Successfully deleted review with ID: {}", reviewId);
    }
}
