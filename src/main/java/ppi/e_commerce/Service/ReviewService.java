package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.CreateReviewRequest;
import ppi.e_commerce.Dto.ReviewDto;
import ppi.e_commerce.Dto.ReviewStatsDto;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(Integer productId, String username, CreateReviewRequest reviewRequest);

    List<ReviewDto> getProductReviews(Integer productId);

    ReviewStatsDto getReviewStats(Integer productId);

    void deleteReview(Integer reviewId);

}
