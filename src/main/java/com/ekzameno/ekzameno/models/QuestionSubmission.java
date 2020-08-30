package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;

public class QuestionSubmission extends Model {
    private String answer;

    /**
     * Create a QuestionSubmission with an ID.
     *
     * @param id ID of the QuestionSubmission
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

    public void setAnswer(String answer) {
        this.answer = answer;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
