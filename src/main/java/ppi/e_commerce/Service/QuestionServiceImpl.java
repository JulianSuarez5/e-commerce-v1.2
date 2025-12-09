package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Answer;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.Question;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.AnswerRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.QuestionRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Question createQuestion(Integer productId, Integer userId, Question question) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        question.setProduct(product);
        question.setUser(user);
        question.setAnswered(false);
        return questionRepository.save(question);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> getProductQuestions(Integer productId) {
        return questionRepository.findByProductIdAndActiveTrueOrderByCreatedAtDesc(productId);
    }

    @Override
    public Question answerQuestion(Integer questionId, Integer userId, String answerText, Boolean isSeller) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setAnswer(answerText);
        answer.setIsSellerAnswer(isSeller != null && isSeller);

        answerRepository.save(answer);
        question.setAnswered(true);
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Integer id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pregunta no encontrada");
        }
        questionRepository.deleteById(id);
    }
}

