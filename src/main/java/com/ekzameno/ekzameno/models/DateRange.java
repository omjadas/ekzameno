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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fromDate == null)
            ? 0
            : fromDate.hashCode());
        result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DateRange other = (DateRange) obj;
        if (fromDate == null) {
            if (other.fromDate != null) {
                return false;
            }
        } else if (!fromDate.equals(other.fromDate)) {
            return false;
        }
        if (toDate == null) {
            if (other.toDate != null) {
                return false;
            }
        } else if (!toDate.equals(other.toDate)) {
            return false;
        }
        return true;
    }
}
