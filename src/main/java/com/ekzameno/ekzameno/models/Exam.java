package com.ekzameno.ekzameno.models;

import java.util.Date;

public class Exam extends Model {
    private String name;
    private Date publishDate;
    private Date closeDate;

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
}
