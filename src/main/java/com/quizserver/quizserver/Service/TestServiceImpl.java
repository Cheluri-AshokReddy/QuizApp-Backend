package com.quizserver.quizserver.Service;

import com.quizserver.quizserver.DTO.*;
import com.quizserver.quizserver.Model.*;
import com.quizserver.quizserver.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final TestResultRepository testResultRepository;
    private final UserRepository userRepository;
    private final TestAttemptRepository testAttemptRepository;


    public TestDTO createTest(TestDTO dto){

        Test test = modelMapper.map(dto, Test.class);
        testRepository.save(test);
        return modelMapper.map(test, TestDTO.class);
    }

    public QuestionDTO addQuestionInTest(QuestionDTO dto) {
        Optional<Test> optionalTest = testRepository.findById(dto.getId()); // Make sure this is testId
        if (optionalTest.isPresent()) {
            Question question = modelMapper.map(dto, Question.class);
            question.setId(null);
            question.setTest(optionalTest.get());
            questionRepository.save(question);
            return modelMapper.map(question, QuestionDTO.class);
        }
        throw new EntityNotFoundException("Test Not Found");
    }

    public List<TestDTO> getAllTests() {
        return testRepository.findAll()
                .stream()
                .peek(test -> test.setTime(test.getQuestions().size() * test.getTime()))
                .collect(toList()).stream().map(test -> modelMapper.map(test, TestDTO.class)).collect(toList());
    }


    public TestDetailsDTO getAllQuestionsByTest(Long id) {
        Optional<Test> optionalTest = testRepository.findById(id);
        if (optionalTest.isEmpty()) {
            throw new EntityNotFoundException("Test not found");
        }

        Test test = optionalTest.get();

        // ✅ Current system time
        LocalDateTime now = LocalDateTime.now();

        // ✅ Validate start & end times
        if (test.getStartTime() != null && now.isBefore(test.getStartTime())) {
            throw new RuntimeException("Test has not started yet. Please check back at " + test.getStartTime());
        }
        if (test.getEndTime() != null && now.isAfter(test.getEndTime())) {
            throw new RuntimeException("Test has already ended.");
        }

        // ✅ Prepare DTO
        TestDTO testDTO = modelMapper.map(test, TestDTO.class);

        // Multiply per-question time with number of questions
        testDTO.setTime(test.getTime() * test.getQuestions().size());

        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        testDetailsDTO.setTestDTO(testDTO);
        testDetailsDTO.setQuestions(
                test.getQuestions().stream()
                        .map(q -> modelMapper.map(q, QuestionDTO.class))
                        .toList()
        );

        return testDetailsDTO;
    }


    public TestResultDTO submitTest(SubmitTestDTO request) {
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new EntityNotFoundException("Test not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // ✅ Check if user has already attempted this test
        Optional<TestAttempt> existingAttemptOpt = testAttemptRepository.findByUserIdAndTestId(user.getId(), test.getId());

        if (existingAttemptOpt.isPresent()) {
            TestAttempt existingAttempt = existingAttemptOpt.get();

            // If status is not RESET → block reattempt
            if (!"RESET".equals(existingAttempt.getStatus())) {
                throw new RuntimeException("User has already attempted this test");
            }

            // If status is RESET → allow reattempt and update record
            existingAttempt.setStatus("COMPLETED");
            existingAttempt.setNoOfAttempts(existingAttempt.getNoOfAttempts() + 1);
            existingAttempt.setAttemptDate(LocalDateTime.now());
            testAttemptRepository.save(existingAttempt);
        }

        int correctAnswers = 0;
        for (QuestionResponse response : request.getResponses()) {
            Question question = questionRepository.findById(response.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            if (Objects.equals(question.getCorrectOption(), response.getSelectedOption())) {
                correctAnswers++;
            }
        }

        int totalQuestions = test.getQuestions().size();
        double percentage = totalQuestions == 0 ? 0.0 : ((double) correctAnswers / totalQuestions) * 100.0;

        // ✅ Save result in results table
        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setTotalQuestions(totalQuestions);
        testResult.setCorrectAnswers(correctAnswers);
        testResult.setPercentage(percentage);
        testResult.setTestName(test.getTitle());
        testResultRepository.save(testResult);

        // ✅ If no existing attempt → create new attempt record
        if (!existingAttemptOpt.isPresent()) {
            TestAttempt attempt = new TestAttempt();
            attempt.setUserId(user.getId());
            attempt.setTest(test);
            attempt.setStatus("COMPLETED");
            attempt.setNoOfAttempts(1);
            attempt.setScore((int) percentage);
            attempt.setAttemptDate(LocalDateTime.now());
            testAttemptRepository.save(attempt);
        } else {
            // If record exists → update score also
            TestAttempt existingAttempt = existingAttemptOpt.get();
            existingAttempt.setScore((int) percentage);
            testAttemptRepository.save(existingAttempt);
        }

        // ✅ Return DTO
        TestResultDTO testResultDTO = modelMapper.map(testResult, TestResultDTO.class);
        testResultDTO.setTestName(test.getTitle());
        return testResultDTO;
    }

    public boolean hasUserAttemptedTest(Long userId, Long testId) {
        return testAttemptRepository.existsByUserIdAndTestId(userId, testId);
    }




    public List<TestResultDTO> getAllTestResults() {
        List<TestResult>testResults = testResultRepository.findAll();
        List<TestResultDTO>testResultDTOS = new ArrayList<>();
        for (TestResult testResult : testResults) {
            TestResultDTO testResultDTO = modelMapper.map(testResult, TestResultDTO.class);
            testResultDTO.setTestName(testResult.getTest().getTitle());
            testResultDTOS.add(testResultDTO);
        }
        return testResultDTOS;
    }

    public List<TestResultDTO> getAllTestResultsOfUser(Long userId) {
        List<TestResult>testResults= testResultRepository.findAllByUserId(userId);
        List<TestResultDTO>testResultDTOS = new ArrayList<>();
        for (TestResult testResult : testResults) {
            TestResultDTO testResultDTO = modelMapper.map(testResult, TestResultDTO.class);
            testResultDTO.setTestName(testResult.getTest().getTitle());
            testResultDTOS.add(testResultDTO);
        }
        return testResultDTOS;
    }

    @Override
    public void deleteTest(Long id) {
        if (!testRepository.existsById(id)) {
            throw new EntityNotFoundException("Test not found with id: " + id);
        }
        testRepository.deleteById(id);
    }


}
