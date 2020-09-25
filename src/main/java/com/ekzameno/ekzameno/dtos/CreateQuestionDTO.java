package com.ekzameno.ekzameno.dtos;

import java.util.List;

/**
 * DTO for creating questions.
 */
public class CreateQuestionDTO {
    public String question;
    public String type;
    public int marks;
    public List<CreateAnswerDTO> answers;
}
