package com.ekzameno.ekzameno.models;

import java.util.UUID;

/**
 * ShortAnswerQuestion for an Exam.
 */
public class ShortAnswerQuestion extends Question {
    public static final String TYPE = "SHORT_ANSWER";

    /**
     * Create a ShortAnswerQuestion with an ID.
     *
     * @param id       ID of the ShortAnswerQuestion
     * @param question question of the ShortAnswerQuestion
     * @param marks    number of marks allocated to the ShortAnswerQuestion
     * @param examId   ID of the related exam
     */
    public ShortAnswerQuestion(
        UUID id,
        String question,
        int marks,
        UUID examId
    ) {
        super(id, question, marks, examId);
    }

    /**
     * Create a ShortAnswerQuestion without an ID (registers as new).
     *
     * @param question question of the ShortAnswerQuestion
     * @param marks    number of marks allocated to the ShortAnswerQuestion
     * @param examId   ID of the related exam
     */
    public ShortAnswerQuestion(String question, int marks, UUID examId) {
        super(question, marks, examId);
    }
}
