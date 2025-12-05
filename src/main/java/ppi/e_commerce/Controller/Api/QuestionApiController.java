package ppi.e_commerce.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.QuestionDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Answer;
import ppi.e_commerce.Model.Question;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Service.QuestionService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/questions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuestionApiController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createQuestion(
            @PathVariable Integer productId,
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            Question question = new Question();
            question.setQuestion(request.get("question"));

            Question created = questionService.createQuestion(productId, user.getId(), question);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear pregunta: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<QuestionDto>> getProductQuestions(@PathVariable Integer productId) {
        List<Question> questions = questionService.getProductQuestions(productId);
        List<QuestionDto> dtos = questions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<?> answerQuestion(
            @PathVariable Integer productId,
            @PathVariable Integer questionId,
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            String answerText = request.get("answer");
            Boolean isSeller = request.containsKey("isSeller") && Boolean.parseBoolean(request.get("isSeller"));

            Question question = questionService.answerQuestion(questionId, user.getId(), answerText, isSeller);
            return ResponseEntity.ok(toDto(question));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al responder pregunta: " + e.getMessage());
        }
    }

    private QuestionDto toDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setProductId(question.getProduct().getId());
        dto.setUserId(question.getUser().getId());
        dto.setUsername(question.getUser().getUsername());
        dto.setQuestion(question.getQuestion());
        dto.setAnswered(question.getAnswered());
        dto.setCreatedAt(question.getCreatedAt());

        if (question.getAnswers() != null) {
            dto.setAnswers(question.getAnswers().stream()
                    .map(this::toAnswerDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private ppi.e_commerce.Dto.AnswerDto toAnswerDto(Answer answer) {
        ppi.e_commerce.Dto.AnswerDto dto = new ppi.e_commerce.Dto.AnswerDto();
        dto.setId(answer.getId());
        dto.setQuestionId(answer.getQuestion().getId());
        dto.setUserId(answer.getUser().getId());
        dto.setUsername(answer.getUser().getUsername());
        dto.setAnswer(answer.getAnswer());
        dto.setIsSellerAnswer(answer.getIsSellerAnswer());
        dto.setCreatedAt(answer.getCreatedAt());
        return dto;
    }
}
