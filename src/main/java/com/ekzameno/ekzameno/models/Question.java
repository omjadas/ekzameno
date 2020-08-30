package com.ekzameno.ekzameno.models;

import java.util.UUID;

public abstract class Question extends Model {
    private String question;
    private int marks;
    private Exam exam;

    public Question(UUID id, String question, int marks, Exam exam) {
        super(id);
        this.question = question;
        this.marks = marks;
        this.exam = exam;
    }

    public Question(String question, int marks, Exam exam) {
        this.question = question;
        this.marks = marks;
        this.exam = exam;
    }

    public String getQuestion() {
        return question;
    }

    public int getMarks() {
        return marks;
    }

    public Exam getExam() {
        return this.exam;
    }
}
