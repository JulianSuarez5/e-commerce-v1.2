package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Model.Answer;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByQuestionIdAndActiveTrueOrderByCreatedAtAsc(Integer questionId);
    List<Answer> findByUserId(Integer userId);
}

