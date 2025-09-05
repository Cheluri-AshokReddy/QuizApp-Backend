package com.quizserver.quizserver.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "test_attempts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "test_id"}))
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String status; // e.g. COMPLETED
    private Integer noOfAttempts = 0;
    private Integer score;
    private LocalDateTime attemptDate = LocalDateTime.now();

    // ✅ Proper relation to Test
    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
