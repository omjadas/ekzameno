package com.ekzameno.ekzameno.models;

import java.util.Date;

/**
 * DateRange for an Exam.
 */
public class DateRange {
    private Date fromDate;
    private Date toDate;

    /**
     * Create a DateRange.
     *
     * @param fromDate the from date for the DateRange
     * @param toDate   the to date for the DateRange
     */
    public DateRange(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
