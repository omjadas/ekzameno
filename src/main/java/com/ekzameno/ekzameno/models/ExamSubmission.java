package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.QuestionSubmissionMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Submission for an Exam.
 */
public class ExamSubmission extends Model {
    private int marks;
    private List<QuestionSubmission> questionSubmissions = null;

    /**
     * Create an ExamSubmission with an ID.
     *
     * @param id ID of the ExamSubmission
     * @param marks total number of marks for the ExamSubmission
     */
    public ExamSubmission(UUID id, int marks) {
        super(id);
        this.marks = marks;
    }

    /**
     * Create an ExamSubmission without an ID (registers as new).
     * @param marks total number of marks for the ExamSubmission.
     */
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

    public void setMarks(int marks) {
        this.marks = marks;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
