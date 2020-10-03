package com.ekzameno.ekzameno.dtos;

import java.util.List;

/**
 * DTO for creating exam submissions.
 */
public class CreateExamSubmissionDTO {
    public Integer marks;
    public List<CreateQuestionSubmissionDTO> answers;
}
