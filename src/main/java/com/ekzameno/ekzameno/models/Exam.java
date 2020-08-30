package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Exam for a Subject.
 */
public class Exam extends Model {
    private String name;
    private DateRange dateRange;
    private List<Question> questions = null;
    private List<ExamSubmission> examSubmissions = null;

    /**
     * Crete an exam with an ID.
     *
     * @param id        ID of the exam
     * @param name      name of the exam
     * @param dateRange date range of the exam
     */
    public Exam(UUID id, String name, DateRange dateRange) {
        super(id);
        this.name = name;
        this.dateRange = dateRange;
    }

    /**
     * Create an exam without an ID (registers as new).
     *
     * @param name      name of the exam
     * @param dateRange date range of the exam
     */
    public Exam(String name, DateRange dateRange) {
        this.name = name;
        this.dateRange = dateRange;
    }

    public String getName() {
        return name;
    }

    public Date getPublishDate() {
        return dateRange.getFromDate();
    }

    public Date getCloseDate() {
        return dateRange.getToDate();
    }

    /**
     * Retrieve the Questions in the Exam.
     *
     * @return Questions in the Exam
     * @throws SQLException if unable to retrieve the Questions
     */
    public List<Question> getQuestions() throws SQLException {
        if (questions == null) {
            return new QuestionMapper().findAllForExam(getId());
        } else {
            return questions;
        }
    }

    /**
     * Retrieve the submissions for the Exam.
     *
     * @return submissions for the Exam
     * @throws SQLException if unable to retrieve the submissions
     */
    public List<ExamSubmission> getExamSubmissions() throws SQLException {
        if (examSubmissions == null) {
            return new ExamSubmissionMapper().findAllForExam(getId());
        } else {
            return examSubmissions;
        }
    }

    /**
     * Set the name of the Exam (marks the Exam as dirty).
     *
     * @param name name of the Exam
     */
    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the publish date of the Exam (marks the Exam as dirty).
     *
     * @param publishDate publish date of the Exam
     */
    public void setPublishDate(Date publishDate) {
        dateRange.setFromDate(publishDate);
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the close date of the Exam (marks the Exam as dirty).
     *
     * @param closeDate close date of the Exam
     */
    public void setCloseDate(Date closeDate) {
        dateRange.setToDate(closeDate);
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
