package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;

public class Answer extends Model {
    private String answer;
    private boolean correct;

    public Answer(UUID id, String answer, boolean correct) {
        super(id);
        this.answer = answer;
        this.correct = correct;
    }

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
