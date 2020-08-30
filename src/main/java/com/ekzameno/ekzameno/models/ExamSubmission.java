package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.QuestionSubmissionMapper;

public class ExamSubmission extends Model {
    private int marks;
    private List<QuestionSubmission> questionSubmissions = null;

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

    public List<QuestionSubmission> getQuestionSubmissions()
            throws SQLException {
        if (questionSubmissions == null) {
            return new QuestionSubmissionMapper()
                .findAllForExamSubmission(getId());
        } else {
            return questionSubmissions;
        }
    }
}
