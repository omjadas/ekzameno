package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Submission for a Question.
 */
public class QuestionSubmission extends Model {
    private String answer;

    /**
     * Create a QuestionSubmission with an ID.
     *
     * @param id     ID of the QuestionSubmission
     * @param answer answer for the QuestionSubmission
     */
    public QuestionSubmission(UUID id, String answer) {
        super(id);
        this.answer = answer;
    }

    /**
     * Create a QuestionSubmission without an ID (registers as new).
     *
     * @param answer answer for the QuestionSubmission
     */
    public QuestionSubmission(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    /**
     * Set the answer for the QuestionSubmission (marks the QuestionSubmission
     * as dirty).
     *
     * @param answer answer for the QuestionSubmission
     */
    public void setAnswer(String answer) {
        this.answer = answer;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
