package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CreateReviewRequest;
import ppi.e_commerce.Dto.ReviewDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Review;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Service.ReviewService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReviewApiController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createReview(
            @PathVariable Integer productId,
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // Verificar si ya hizo review
            if (reviewService.hasUserReviewed(productId, user.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ya has realizado una reseña para este producto");
            }

            Review review = new Review();
            review.setRating(request.getRating());
            review.setTitle(request.getTitle());
            review.setComment(request.getComment());
            review.setVerifiedPurchase(false); //TODO: Verificar si compró el producto 

            Review created = reviewService.createReview(productId, user.getId(), review);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear reseña: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getProductReviews(@PathVariable Integer productId) {
        List<Review> reviews = reviewService.getProductReviews(productId);
        List<ReviewDto> dtos = reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getReviewStats(@PathVariable Integer productId) {
        Double avgRating = reviewService.getAverageRating(productId);
        Long count = reviewService.getReviewCount(productId);

        return ResponseEntity.ok(new ReviewStats(avgRating, count));
    }

    private ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());
        dto.setVerifiedPurchase(review.getVerifiedPurchase());
        dto.setHelpfulCount(review.getHelpfulCount());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    private static class ReviewStats {

        private final Double averageRating;
        private final Long totalReviews;

        public ReviewStats(Double averageRating, Long totalReviews) {
            this.averageRating = averageRating;
            this.totalReviews = totalReviews;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public Long getTotalReviews() {
            return totalReviews;
        }
    }
}
