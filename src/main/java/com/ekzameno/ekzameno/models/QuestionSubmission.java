package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Submission for a Question.
 */
public class QuestionSubmission extends Model {
    private String answer;
    private UUID questionId;
    private UUID examSubmissionId;
    @JsonIgnore
    private Question question = null;
    @JsonIgnore
    private ExamSubmission examSubmission = null;
    private int mark;

    /**
     * Create a QuestionSubmission with an ID.
     *
     * @param id               ID of the QuestionSubmission
     * @param answer           answer for the QuestionSubmission
     * @param questionId       ID of the related question
     * @param examSubmissionId ID of the related examSubmission
     * @param mark             Mark for the question
     */
    public QuestionSubmission(
        UUID id,
        String answer,
        UUID questionId,
        UUID examSubmissionId,
        int mark
    ) {
        super(id);
        this.answer = answer;
        this.questionId = questionId;
        this.examSubmissionId = examSubmissionId;
        this.mark = mark;
    }

    /**
     * Create a QuestionSubmission without an ID (registers as new).
     *
     * @param answer           answer for the QuestionSubmission
     * @param questionId       ID of the related question
     * @param examSubmissionId ID of the related examSubmission
     * @param mark             Mark for the question
     */
    public QuestionSubmission(
        String answer,
        UUID questionId,
        UUID examSubmissionId,
        Integer mark
    ) {
        this.answer = answer;
        this.questionId = questionId;
        this.examSubmissionId = examSubmissionId;
        this.mark = mark;
    }

    public String getAnswer() {
        return answer;
    }

    public Integer getMark() {
        return mark;
    }

    /**
     * Set the mark for the question.
     *
     * @param mark Mark for the question
     */
    public void setMark(Integer mark) {
        this.mark = mark;
        UnitOfWork.getCurrent().registerDirty(this);
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

    /**
     * Retrieve the related question.
     *
     * @return the related question
     * @throws SQLException if unable to retrieve the question
     */
    public Question getQuestion() throws SQLException {
        if (question == null) {
            question = new QuestionMapper().findById(questionId);
        }
        return question;
    }

    /**
     * Retrieve the related exam submission.
     *
     * @return the related exam submission.
     * @throws SQLException if unable to retrieve the exam submission
     */
    public ExamSubmission getExamSubmission() throws SQLException {
        if (examSubmission == null) {
            examSubmission = new ExamSubmissionMapper().findById(
                examSubmissionId
            );
        }
        return examSubmission;
    }

    /**
     * Set the ID of the related question (marks the QuestionSubmission as
     * dirty).
     *
     * @param questionId ID of the related question
     */
    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
        this.question = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the ID of the related exam submission (marks the QuestionSubmission
     * as dirty).
     *
     * @param examSubmissionId ID of the related exam submission
     */
    public void setExamSubmissionId(UUID examSubmissionId) {
        this.examSubmissionId = examSubmissionId;
        this.examSubmission = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the related question (marks the QuestionSubmission as dirty).
     *
     * @param question the related question
     */
    public void setQuestion(Question question) {
        this.question = question;
        this.questionId = question.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the related exam submission (marks the QuestionSubmission as dirty).
     *
     * @param examSubmission the related exam submission
     */
    public void setExamSubmission(ExamSubmission examSubmission) {
        this.examSubmission = examSubmission;
        this.examSubmissionId = examSubmission.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((answer == null) ? 0 : answer.hashCode());
        result = prime * result + ((examSubmissionId == null)
            ? 0
            : examSubmissionId.hashCode());
        result = prime * result + ((questionId == null)
            ? 0
            : questionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        QuestionSubmission other = (QuestionSubmission) obj;
        if (answer == null) {
            if (other.answer != null) {
                return false;
            }
        } else if (!answer.equals(other.answer)) {
            return false;
        }
        if (examSubmissionId == null) {
            if (other.examSubmissionId != null) {
                return false;
            }
        } else if (!examSubmissionId.equals(other.examSubmissionId)) {
            return false;
        }
        if (questionId == null) {
            if (other.questionId != null) {
                return false;
            }
        } else if (!questionId.equals(other.questionId)) {
            return false;
        }
        return true;
    }
}
