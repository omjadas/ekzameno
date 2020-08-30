package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.QuestionSubmissionMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

public abstract class Question extends Model {
    private String question;
    private int marks;
    private List<QuestionSubmission> questionSubmissions = null;

    /**
     * Create a Question with an ID.
     *
     * @param id ID of the Question
     * @param question question of the Question
     * @param marks number of marks allocated to the Question
     */
    public Question(UUID id, String question, int marks) {
        super(id);
        this.question = question;
        this.marks = marks;
    }

    /**
     * Create a Question without an ID (registers as new).
     *
     * @param question question of the Question
     * @param marks number of marks allocated to the Question
     */
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

    public List<QuestionSubmission> getQuestionSubmissions()
            throws SQLException {
        if (questionSubmissions == null) {
            return new QuestionSubmissionMapper().findAllForQuestion(getId());
        } else {
            return questionSubmissions;
        }
    }

    public void setQuestion(String question) {
        this.question = question;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setMarks(int marks) {
        this.marks = marks;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
