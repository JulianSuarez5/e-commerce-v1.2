package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByProductIdAndActiveTrueOrderByCreatedAtDesc(Integer productId);
    List<Question> findByProductIdAndAnsweredFalseAndActiveTrue(Integer productId);
    List<Question> findByUserId(Integer userId);
}

