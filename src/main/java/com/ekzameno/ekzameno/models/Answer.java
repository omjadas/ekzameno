package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Answer extends Model {
    private String answer;
    private Boolean correct;

    public Answer(UUID id, String answer, Boolean correct) {
        super(id);
        this.answer = answer;
        this.correct = correct;
    }

    public Answer(String answer, Boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public String getAnswer() {
        return answer;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
