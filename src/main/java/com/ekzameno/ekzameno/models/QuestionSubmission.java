package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class QuestionSubmission extends Model {
    private String answer;

    public QuestionSubmission(UUID id, String answer) {
        super(id);
        this.answer = answer;
    }

    public QuestionSubmission(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public Question getQuestion() {
        return null;
    }
}
