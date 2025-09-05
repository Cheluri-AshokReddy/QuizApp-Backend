package com.quizserver.quizserver.DTO;

import lombok.Data;

@Data
public class QuestionDTO {

    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
}
