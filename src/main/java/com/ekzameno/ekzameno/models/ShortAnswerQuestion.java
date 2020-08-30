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
     */
    public ShortAnswerQuestion(UUID id, String question, int marks) {
        super(id, question, marks);
    }

    /**
     * Create a ShortAnswerQuestion without an ID (registers as new).
     *
     * @param question question of the ShortAnswerQuestion
     * @param marks    number of marks allocated to the ShortAnswerQuestion
     */
    public ShortAnswerQuestion(String question, int marks) {
        super(question, marks);
    }
}
