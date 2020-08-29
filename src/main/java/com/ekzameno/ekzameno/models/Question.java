package com.ekzameno.ekzameno.models;

public abstract class Question extends Model {
    private String question;
    private int marks;

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
