package com.quizserver.quizserver.Repository;

import com.quizserver.quizserver.Model.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    boolean existsByUserIdAndTestId(Long userId, Long testId);


    Optional<TestAttempt> findByUserIdAndTestId(Long id, Long id1);
}

