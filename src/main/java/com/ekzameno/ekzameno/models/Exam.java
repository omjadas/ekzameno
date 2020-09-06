package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.proxies.ExamSubmissionExamProxyList;
import com.ekzameno.ekzameno.proxies.ProxyList;
import com.ekzameno.ekzameno.proxies.QuestionProxyList;
import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.github.slugify.Slugify;

/**
 * Exam for a Subject.
 */
public class Exam extends Model {
    private String name;
    private String slug;
    private DateRange dateRange;
    private ProxyList<Question> questions;
    private ProxyList<ExamSubmission> examSubmissions;
    private UUID subjectId;
    private Subject subject = null;

    /**
     * Crete an exam with an ID.
     *
     * @param id        ID of the exam
     * @param name      name of the exam
     * @param dateRange date range of the exam
     * @param subjectId ID of the related subject
     * @param slug      slug for the exam
     */
    public Exam(
        UUID id,
        String name,
        DateRange dateRange,
        UUID subjectId,
        String slug
    ) {
        super(id);
        this.name = name;
        this.slug = slug;
        this.dateRange = dateRange;
        this.subjectId = subjectId;
        this.questions = new QuestionProxyList(id);
        this.examSubmissions = new ExamSubmissionExamProxyList(id);
    }

    /**
     * Create an exam without an ID (registers as new).
     *
     * @param name      name of the exam
     * @param dateRange date range of the exam
     * @param subjectId ID of the related subject
     */
    public Exam(String name, DateRange dateRange, UUID subjectId) {
        this.name = name;
        this.slug = new Slugify().slugify(name);
        this.dateRange = dateRange;
        this.subjectId = subjectId;
        this.questions = new QuestionProxyList(getId());
        this.examSubmissions = new ExamSubmissionExamProxyList(getId());
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
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
     */
    public ProxyList<Question> getQuestions() {
        return questions;
    }

    /**
     * Retrieve the submissions for the Exam.
     *
     * @return submissions for the Exam
     */
    public ProxyList<ExamSubmission> getExamSubmissions() {
        return examSubmissions;
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

    public UUID getSubjectId() {
        return subjectId;
    }

    /**
     * Retrieve the related subject.
     *
     * @return the related subject
     * @throws SQLException if unable to retrieve the subject
     */
    public Subject getSubject() throws SQLException {
        if (subject == null) {
            subject = new SubjectMapper().findById(subjectId);
        }
        return subject;
    }

    /**
     * Set the ID of the related subject (marks the Exam as dirty).
     *
     * @param subjectId ID of the related subject
     */
    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
        this.subject = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the related subject (marks the Exam as dirty).
     *
     * @param subject the related subject.
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subjectId = subject.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
