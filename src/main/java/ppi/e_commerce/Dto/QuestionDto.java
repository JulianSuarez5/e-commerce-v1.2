package ppi.e_commerce.Dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDto {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private String username;
    private String question;
    private Boolean answered;
    private LocalDateTime createdAt;
    private List<ppi.e_commerce.Dto.AnswerDto> answers;

    public QuestionDto() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public Boolean getAnswered() { return answered; }
    public void setAnswered(Boolean answered) { this.answered = answered; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ppi.e_commerce.Dto.AnswerDto> getAnswers() { return answers; }
    public void setAnswers(List<ppi.e_commerce.Dto.AnswerDto> answers) { this.answers = answers; }
}

