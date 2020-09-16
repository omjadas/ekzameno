package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import com.ekzameno.ekzameno.models.DateRange;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service to handle authentication.
 */
public class ExamService {
    /**
     * Create an exam for a given subject.
     *
     * @param name        name of the exam
     * @param description description of the exam
     * @param startTime   publish date of the exam
     * @param finishTime  close date of the exam
     * @param subjectId   id of the subject
     * @return a new exam
     */
    public Exam createExam(
        String name,
        String description,
        Date startTime,
        Date finishTime,
        UUID subjectId
    ) {
        try (DBConnection connection = DBConnection.getInstance()) {
            DateRange dateRange = new DateRange(startTime, finishTime);
            Exam exam = new Exam(name, description, dateRange, subjectId);
            UnitOfWork.getCurrent().commit();
            return exam;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
