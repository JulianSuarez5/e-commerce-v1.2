package ppi.e_commerce.Service;

import ppi.e_commerce.Model.Question;
import java.util.List;

public interface QuestionService {
    Question createQuestion(Integer productId, Integer userId, Question question);
    List<Question> getProductQuestions(Integer productId);
    Question answerQuestion(Integer questionId, Integer userId, String answer, Boolean isSeller);
    void deleteQuestion(Integer id);
}

