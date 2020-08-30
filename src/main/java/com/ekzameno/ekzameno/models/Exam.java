package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

public class Exam extends Model {
    private String name;
    private Date publishDate;
    private Date closeDate;
    private List<Question> questions = null;
    private List<ExamSubmission> examSubmissions = null;

    public Exam(UUID id, String name, Date publishDate, Date closeDate) {
        super(id);
        this.name = name;
        this.publishDate = publishDate;
        this.closeDate = closeDate;
    }

    public Exam(String name, Date publishDate, Date closeDate) {
        this.name = name;
        this.publishDate = publishDate;
        this.closeDate = closeDate;
    }

    public String getName() {
        return name;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public List<Question> getQuestions() throws SQLException {
        if (questions == null) {
            return new QuestionMapper().findAllForExam(getId());
        } else {
            return questions;
        }
    }

    public List<ExamSubmission> getExamSubmissions() throws SQLException {
        if (examSubmissions == null) {
            return new ExamSubmissionMapper().findAllForExam(getId());
        } else {
            return examSubmissions;
        }
    }

    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
