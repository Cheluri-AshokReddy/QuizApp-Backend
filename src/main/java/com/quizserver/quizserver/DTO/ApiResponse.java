package com.quizserver.quizserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;   // SUCCESS or ERROR
    private String message;  // Friendly message
    private T data;          // Optional (test result, etc.)
}

