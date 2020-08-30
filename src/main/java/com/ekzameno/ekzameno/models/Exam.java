package com.ekzameno.ekzameno.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Exam extends Model {
    private String name;
    private Date publishDate;
    private Date closeDate;
    private Subject subject;

    public Exam(
        UUID id,
        String name,
        Date publishDate,
        Date closeDate,
        Subject subject
    ) {
        super(id);
        this.name = name;
        this.publishDate = publishDate;
        this.closeDate = closeDate;
        this.subject = subject;
    }

    public Exam(
        String name,
        Date publishDate,
        Date closeDate,
        Subject subject
    ) {
        this.name = name;
        this.publishDate = publishDate;
        this.closeDate = closeDate;
        this.subject = subject;
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

    public Subject getSubject() {
        return subject;
    }

    public List<Question> getQuestions() {
        return null;
    }
}
