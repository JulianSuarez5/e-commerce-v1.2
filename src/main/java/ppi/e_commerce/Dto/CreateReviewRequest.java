package ppi.e_commerce.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReviewRequest {

    @NotNull(message = "El rating es obligatorio")
    @Min(value = 1, message = "El rating debe ser al menos 1")
    @Max(value = 5, message = "El rating debe ser máximo 5")
    private Integer rating;

    @Size(max = 200)
    private String title;

    @Size(max = 1000)
    private String comment;

    public CreateReviewRequest() {
    }

    // Getters and Setters
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
