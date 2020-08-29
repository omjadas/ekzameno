package com.ekzameno.ekzameno.models;

import java.util.UUID;

public abstract class Question extends Model {
    private String question;
    private int marks;

    public Question(UUID id, String question, int marks) {
        super(id);
        this.question = question;
        this.marks = marks;
    }

    public Question(String question, int marks) {
        this.question = question;
        this.marks = marks;
    }

    public String getQuestion() {
        return question;
    }

    public int getMarks() {
        return marks;
    }
}
