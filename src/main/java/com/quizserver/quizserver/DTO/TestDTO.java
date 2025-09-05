package com.quizserver.quizserver.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestDTO {
    private Long id;
    private String title;
    private String description;
    private Long time;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}