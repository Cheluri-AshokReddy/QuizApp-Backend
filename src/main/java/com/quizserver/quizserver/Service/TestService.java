package com.quizserver.quizserver.Service;

import com.quizserver.quizserver.DTO.*;
import com.quizserver.quizserver.Model.Test;

import java.util.List;

public interface TestService {
    TestDTO createTest(TestDTO dto);
    QuestionDTO addQuestionInTest(QuestionDTO dto);
    List<TestDTO> getAllTests();
    TestDetailsDTO getAllQuestionsByTest(Long id);
    TestResultDTO submitTest(SubmitTestDTO request);
    List<TestResultDTO> getAllTestResults();
    List<TestResultDTO> getAllTestResultsOfUser(Long userId);
    boolean hasUserAttemptedTest(Long userId, Long testId);
    void deleteTest(Long id);

}
