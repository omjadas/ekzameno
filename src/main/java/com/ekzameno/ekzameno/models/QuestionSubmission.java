package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Submission for a Question.
 */
public class QuestionSubmission extends Model {
    private String answer;
    private UUID questionId;
    private UUID examSubmissionId;
    private Question question = null;
    private ExamSubmission examSubmission = null;

    /**
     * Create a QuestionSubmission with an ID.
     *
     * @param id     ID of the QuestionSubmission
     * @param answer answer for the QuestionSubmission
     */
    public QuestionSubmission(
            UUID id,
            String answer,
            UUID questionId,
            UUID examSubmissionId) {
        super(id);
        this.answer = answer;
        this.questionId = questionId;
        this.examSubmissionId = examSubmissionId;
    }

    /**
     * Create a QuestionSubmission without an ID (registers as new).
     *
     * @param answer answer for the QuestionSubmission
     */
    public QuestionSubmission(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    /**
     * Set the answer for the QuestionSubmission (marks the QuestionSubmission
     * as dirty).
     *
     * @param answer answer for the QuestionSubmission
     */
    public void setAnswer(String answer) {
        this.answer = answer;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public UUID getExamSubmissionId() {
        return examSubmissionId;
    }

    public Question getQuestion() throws SQLException {
        if (question == null) {
            question = new QuestionMapper().find(questionId);
        }
        return question;
    }

    public ExamSubmission getExamSubmission() throws SQLException {
        if (examSubmission == null) {
            examSubmission = new ExamSubmissionMapper().find(examSubmissionId);
        }
        return examSubmission;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
        this.question = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setExamSubmissionId(UUID examSubmissionId) {
        this.examSubmissionId = examSubmissionId;
        this.examSubmission = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setQuestion(Question question) {
        this.question = question;
        this.questionId = question.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setExamSubmission(ExamSubmission examSubmission) {
        this.examSubmission = examSubmission;
        this.examSubmissionId = examSubmission.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
