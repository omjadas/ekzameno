package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Answer extends Model {
    private String answer;

    public Answer(UUID id, String answer) {
        setId(id);
        this.answer = answer;
    }

    public Answer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
