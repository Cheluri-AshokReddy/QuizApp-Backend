package com.quizserver.quizserver.DTO;

import lombok.Data;

@Data
public class QuestionResponse {

    private Long questionId;
    private String selectedOption;
}
