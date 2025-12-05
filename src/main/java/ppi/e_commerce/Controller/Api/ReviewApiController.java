package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CreateReviewRequest;
import ppi.e_commerce.Dto.ReviewDto;
import ppi.e_commerce.Dto.ReviewStatsDto;
import ppi.e_commerce.Service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products/{productId}/reviews")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReviewApiController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewApiController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable Integer productId,
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        log.info("POST /api/products/{}/reviews invoked by user: {}", productId, authentication.getName());
        ReviewDto createdReview = reviewService.createReview(productId, authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getProductReviews(@PathVariable Integer productId) {
        log.info("GET /api/products/{}/reviews invoked", productId);
        List<ReviewDto> reviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/stats")
    public ResponseEntity<ReviewStatsDto> getReviewStats(@PathVariable Integer productId) {
        log.info("GET /api/products/{}/reviews/stats invoked", productId);
        ReviewStatsDto stats = reviewService.getReviewStats(productId);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Integer productId, // Keep for URL structure, though not used in service logic
            @PathVariable Integer reviewId) {
        log.info("DELETE /api/products/{}/reviews/{} invoked", productId, reviewId);
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
