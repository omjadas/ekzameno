package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.models.DateRange;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.ExamSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service to handle exams.
 */
public class ExamService {
    private ExamMapper examMapper = new ExamMapper();

    /**
     * Fetches an exam for a given slug.
     *
     * @param slug exam's slug
     * @return exam
     */
    public Exam getExam(String slug)
        throws NotFoundException, InternalServerErrorException {
        try (DBConnection connection = DBConnection.getInstance()) {
            return examMapper.findBySlug(slug);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Retrieve all Exams.
     *
     * @param subjectId id of the subject
     * @return all exams for the subject
     */
    public List<Exam> getExamsForSubject(UUID subjectId) {
        try (DBConnection connection = DBConnection.getInstance()) {
            return examMapper.findAllForSubject(subjectId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieve all Published Exams.
     *
     * @param subjectId id of the subject
     * @return all exams for the subject
     */
    public List<Exam> getPublishedExamsForSubject(UUID subjectId) {
        try (DBConnection connection = DBConnection.getInstance()) {
            return examMapper.findAllPublishedExams(subjectId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update an exam for a given subject.
     *
     * @param name        name of the exam
     * @param description description of the exam
     * @param startTime   publish date of the exam
     * @param finishTime  close date of the exam
     * @param examId      id of the exam
     * @return a new exam
     */
    public Exam updateExam(
        String name,
        String description,
        Date startTime,
        Date finishTime,
        UUID examId
    ) {
        try (DBConnection connection = DBConnection.getInstance()) {
            Exam exam = examMapper.findById(examId);
            exam.setName(name);
            exam.setDescription(description);
            exam.setStartTime(startTime);
            exam.setFinishTime(finishTime);
            UnitOfWork.getCurrent().commit();
            return exam;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Delete an exam for a given subject.
     *
     * @param examId id of the exam
     */
    public void deleteExam(
        UUID examId
    ) {
        try (DBConnection connection = DBConnection.getInstance()) {
            Exam exam = examMapper.findById(examId);
            UnitOfWork.getCurrent().registerDeleted(exam);
            UnitOfWork.getCurrent().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Create a submission for a given exam.
     *
     * @param examId    id of the Exam
     * @param studentId id of the question
     * @return a new exam submission
     */
    public ExamSubmission createSubmission(
        UUID examId,
        UUID studentId
    ) {
        try (DBConnection connection = DBConnection.getInstance()) {
            ExamSubmission examSubmission = new ExamSubmission(
                0,
                studentId,
                examId
            );
            UnitOfWork.getCurrent().commit();
            return examSubmission;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }
}
