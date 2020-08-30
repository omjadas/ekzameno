package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Answer for a MultipleChoiceQuestion.
 */
public class Answer extends Model {
    private String answer;
    private boolean correct;

    /**
     * Create an Answer with an ID.
     *
     * @param id ID of the Answer
     * @param answer answer of the Answer
     * @param correct whether the Answer is the correct answer for the question
     */
    public Answer(UUID id, String answer, boolean correct) {
        super(id);
        this.answer = answer;
        this.correct = correct;
    }

    /**
     * Create an Answer without an ID (registers as new).
     *
     * @param answer answer of the Answer
     * @param correct whether the Answer is the correct answer for the question
     */
    public Answer(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
