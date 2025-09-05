package com.quizserver.quizserver.Controller;

import com.quizserver.quizserver.DTO.*;
import com.quizserver.quizserver.Service.TestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/test")
@CrossOrigin("*")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping
    public ResponseEntity<TestDTO> createTest(@RequestBody TestDTO dto) {
        try {
            TestDTO savedDto = testService.createTest(dto);
            return new ResponseEntity<>(savedDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/question")
    public ResponseEntity<?> addQuestionInTest(@RequestBody QuestionDTO dto) {
        try {
            QuestionDTO savedQuestion = testService.addQuestionInTest(dto);
            return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllTests() {
        try {
            return new ResponseEntity<>(testService.getAllTests(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllQuestions(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>("SUCCESS", "Test questions fetched successfully",
                            testService.getAllQuestionsByTest(id))
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Something went wrong", null));
        }
    }


    @PostMapping("/submit-test")
    public ResponseEntity<ApiResponse<?>> submitTest(@RequestBody SubmitTestDTO dto) {
        try {
            TestResultDTO result = testService.submitTest(dto);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Test submitted successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Something went wrong", null));
        }
    }

    @GetMapping("/status/{userId}/{testId}")
    public ResponseEntity<?> getTestStatus(@PathVariable Long userId, @PathVariable Long testId) {
        try {
            boolean attempted = testService.hasUserAttemptedTest(userId, testId);

            Map<String, Object> response = new HashMap<>();
            response.put("attempted", attempted);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/test-result")
    public ResponseEntity<?> getAllTestResults() {
        try {
            return new ResponseEntity<>(testService.getAllTestResults(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test-result/{id}")
    public ResponseEntity<?> getAllTestResultsOfUser(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(testService.getAllTestResultsOfUser(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTest(id);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Test deleted successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("ERROR", "Something went wrong", null));
        }
    }

}

