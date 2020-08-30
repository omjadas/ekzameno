package com.ekzameno.ekzameno.models;

import java.util.UUID;

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
    }
}
