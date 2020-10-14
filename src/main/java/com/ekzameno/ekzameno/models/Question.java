package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.proxies.ProxyList;
import com.ekzameno.ekzameno.proxies.QuestionSubmissionQuestionProxyList;
import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Question for an Exam.
 */
public abstract class Question extends Model {
    private String question;
    private int marks;
    @JsonIgnore
    private ProxyList<QuestionSubmission> questionSubmissions;
    private UUID examId;
    @JsonIgnore
    private Exam exam = null;
    private final String type;

    /**
     * Create a Question with an ID.
     *
     * @param id       ID of the Question
     * @param question question of the Question
     * @param marks    number of marks allocated to the Question
     * @param examId   ID of the related exam
     * @param type     type of the question
     */
    public Question(
        UUID id,
        String question,
        int marks,
        UUID examId,
        String type
    ) {
        super(id);
        this.question = question;
        this.marks = marks;
        this.examId = examId;
        this.type = type;
        this.questionSubmissions = new QuestionSubmissionQuestionProxyList(id);
    }

    /**
     * Create a Question without an ID (registers as new).
     *
     * @param question question of the Question
     * @param marks    number of marks allocated to the Question
     * @param examId   ID of the related exam
     * @param type     type of the question
     */
    public Question(String question, int marks, UUID examId, String type) {
        this.question = question;
        this.marks = marks;
        this.examId = examId;
        this.type = type;
        this.questionSubmissions = new QuestionSubmissionQuestionProxyList(
            getId()
        );
    }

    public String getQuestion() {
        return question;
    }

    public int getMarks() {
        return marks;
    }

    public String getType() {
        return type;
    }

    /**
     * Retrieve submissions for the Question.
     *
     * @return submissions for the Question
     */
    public ProxyList<QuestionSubmission> getQuestionSubmissions() {
        return questionSubmissions;
    }

    /**
     * Set the question for the Question (marks the Question as dirty).
     *
     * @param question question to set
     */
    public void setQuestion(String question) {
        this.question = question;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the number of marks the question is worth (marks the Question as
     * dirty).
     *
     * @param marks number of marks the question is worth
     */
    public void setMarks(int marks) {
        this.marks = marks;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public UUID getExamId() {
        return examId;
    }

    /**
     * Retrieve the related question.
     *
     * @return the related question
     * @throws SQLException if unable to retrieve the question
     */
    public Exam getExam() throws SQLException {
        if (exam == null) {
            exam = new ExamMapper().findById(examId);
        }
        return exam;
    }

    /**
     * Set the ID of the related exam (marks the Question as dirty).
     *
     * @param examId ID of the related exam
     */
    public void setExamId(UUID examId) {
        this.examId = examId;
        this.exam = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the related exam (marks the Question as dirty).
     *
     * @param exam related exam
     */
    public void setExam(Exam exam) {
        this.exam = exam;
        this.examId = exam.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((examId == null) ? 0 : examId.hashCode());
        result = prime * result + marks;
        result = prime * result + ((question == null)
            ? 0
            : question.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Question other = (Question) obj;
        if (examId == null) {
            if (other.examId != null) {
                return false;
            }
        } else if (!examId.equals(other.examId)) {
            return false;
        }
        if (marks != other.marks) {
            return false;
        }
        if (question == null) {
            if (other.question != null) {
                return false;
            }
        } else if (!question.equals(other.question)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
