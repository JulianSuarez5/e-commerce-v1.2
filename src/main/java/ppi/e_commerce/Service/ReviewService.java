package ppi.e_commerce.Service;

import ppi.e_commerce.Model.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review createReview(Integer productId, Integer userId, Review review);
    List<Review> getProductReviews(Integer productId);
    Optional<Review> findById(Integer id);
    Review updateReview(Review review);
    void deleteReview(Integer id);
    Double getAverageRating(Integer productId);
    Long getReviewCount(Integer productId);
    boolean hasUserReviewed(Integer productId, Integer userId);
}

