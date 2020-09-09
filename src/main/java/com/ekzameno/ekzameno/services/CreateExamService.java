package com.ekzameno.ekzameno.services;

import java.sql.SQLException;

import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.models.DateRange;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service to handle authentication.
 */
public class CreateExamService {
    private ExamMapper examMapper = new ExamMapper();

    /**
     * Create a new Exam
     *
     * @param exam Details of Exam
     */
    public void CreateanExam(Exam exam) {
        try (DBConnection connection = DBConnection.getInstance()) {
            examMapper.insert(exam);
            UnitOfWork.getCurrent().commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}