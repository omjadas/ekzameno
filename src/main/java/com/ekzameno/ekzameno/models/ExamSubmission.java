package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class ExamSubmission extends Model {
    private int marks;

    public ExamSubmission(UUID id, int marks) {
        super(id);
        this.marks = marks;
    }

    public ExamSubmission(int marks) {
        this.marks = marks;
    }

    public int getMarks() {
        return marks;
    }

    public Student getStudent() {
        return null;
    }

    public Exam getExam() {
        return null;
    }
}
