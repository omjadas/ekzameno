package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.proxies.ExamSubmissionExamProxyList;
import com.ekzameno.ekzameno.proxies.ProxyList;
import com.ekzameno.ekzameno.proxies.QuestionProxyList;
import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.slugify.Slugify;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Exam for a Subject.
 */
public class Exam extends Model {
    private String name;
    private String description;
    private String slug;
    private DateRange dateRange;
    @JsonIgnore
    private ProxyList<Question> questions;
    @JsonIgnore
    private ProxyList<ExamSubmission> examSubmissions;
    private UUID subjectId;
    @JsonIgnore
    private Subject subject = null;

    /**
     * Crete an exam with an ID.
     *
     * @param id          ID of the exam
     * @param name        name of the exam
     * @param description description of the exam
     * @param dateRange   date range of the exam
     * @param subjectId   ID of the related subject
     * @param slug        slug for the exam
     */
    public Exam(
        UUID id,
        String name,
        String description,
        DateRange dateRange,
        UUID subjectId,
        String slug
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.dateRange = dateRange;
        this.subjectId = subjectId;
        this.questions = new QuestionProxyList(id);
        this.examSubmissions = new ExamSubmissionExamProxyList(id);
    }

    /**
     * Create an exam without an ID (registers as new).
     *
     * @param name        name of the exam
     * @param description description of the exam
     * @param dateRange   date range of the exam
     * @param subjectId   ID of the related subject
     */
    public Exam(
        String name,
        String description,
        DateRange dateRange,
        UUID subjectId
    ) {
        this.name = name;
        this.description = description;
        this.slug = new Slugify().slugify(name) + "-" +
            RandomStringUtils.randomAlphanumeric(8);

        this.dateRange = dateRange;
        this.subjectId = subjectId;
        this.questions = new QuestionProxyList(getId());
        this.examSubmissions = new ExamSubmissionExamProxyList(getId());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSlug() {
        return slug;
    }

    public Date getStartTime() {
        return dateRange.getFromDate();
    }

    public Date getFinishTime() {
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
     * Set the description of the Exam (marks the Exam as dirty).
     *
     * @param description name of the Exam
     */
    public void setDescription(String description) {
        this.description = description;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the publish date of the Exam (marks the Exam as dirty).
     *
     * @param startTime publish date of the Exam
     */
    public void setStartTime(Date startTime) {
        dateRange.setFromDate(startTime);
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the close date of the Exam (marks the Exam as dirty).
     *
     * @param finishTime close date of the Exam
     */
    public void setFinishTime(Date finishTime) {
        dateRange.setToDate(finishTime);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((dateRange == null)
            ? 0
            : dateRange.hashCode());
        result = prime * result + ((description == null)
            ? 0
            : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((slug == null) ? 0 : slug.hashCode());
        result = prime * result + ((subjectId == null)
            ? 0
            : subjectId.hashCode());
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
        Exam other = (Exam) obj;
        if (dateRange == null) {
            if (other.dateRange != null) {
                return false;
            }
        } else if (!dateRange.equals(other.dateRange)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (slug == null) {
            if (other.slug != null) {
                return false;
            }
        } else if (!slug.equals(other.slug)) {
            return false;
        }
        if (subjectId == null) {
            if (other.subjectId != null) {
                return false;
            }
        } else if (!subjectId.equals(other.subjectId)) {
            return false;
        }
        return true;
    }
}
