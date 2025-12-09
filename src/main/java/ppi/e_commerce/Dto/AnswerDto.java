package ppi.e_commerce.Dto;

import java.time.LocalDateTime;

public class AnswerDto {
    private Integer id;
    private Integer questionId;
    private Integer userId;
    private String username;
    private String answer;
    private Boolean isSellerAnswer;
    private LocalDateTime createdAt;

    public AnswerDto() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public Boolean getIsSellerAnswer() { return isSellerAnswer; }
    public void setIsSellerAnswer(Boolean isSellerAnswer) { this.isSellerAnswer = isSellerAnswer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

