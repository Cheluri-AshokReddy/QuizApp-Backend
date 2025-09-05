package com.quizserver.quizserver.Repository;
import com.quizserver.quizserver.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
