package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.dtos.CreateQuestionSubmissionDTO;
import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.models.DateRange;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.ExamSubmission;
import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service to handle exams.
 */
public class ExamService {
    private ExamMapper examMapper = new ExamMapper();
    private ExamSubmissionMapper examSubmissionMapper =
        new ExamSubmissionMapper();

    /**
     * Fetches an exam for a given slug.
     *
     * @param slug exam's slug
     * @return exam
     */
    public Exam getExam(String slug)
        throws NotFoundException, InternalServerErrorException {
        try (DBConnection connection = DBConnection.getCurrent()) {
            return examMapper.findBySlug(slug);
        } catch (SQLException e) {
            e.printStackTrace();
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
        try (DBConnection connection = DBConnection.getCurrent()) {
            return examMapper.findAllForSubject(subjectId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Retrieve all Published Exams.
     *
     * @param subjectId id of the subject
     * @return all exams for the subject
     */
    public List<Exam> getPublishedExamsForSubject(UUID subjectId) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            return examMapper.findAllPublishedExams(subjectId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
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
        try (DBConnection connection = DBConnection.getCurrent()) {
            DateRange dateRange = new DateRange(startTime, finishTime);
            Exam exam = new Exam(name, description, dateRange, subjectId);
            UnitOfWork.getCurrent().commit();
            return exam;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

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
        try (DBConnection connection = DBConnection.getCurrent()) {
            Exam exam = examMapper.findById(examId);
            exam.setName(name);
            exam.setDescription(description);
            exam.setStartTime(startTime);
            exam.setFinishTime(finishTime);
            UnitOfWork.getCurrent().commit();
            return exam;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

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
        try (DBConnection connection = DBConnection.getCurrent()) {
            Exam exam = examMapper.findById(examId);
            UnitOfWork.getCurrent().registerDeleted(exam);
            UnitOfWork.getCurrent().commit();
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Create a submission for a given exam.
     *
     * @param examId    id of the Exam
     * @param studentId id of the question
     * @param marks     number of marks for the submission
     * @param answers   answers
     * @return a new exam submission
     */
    public ExamSubmission createSubmission(
        UUID examId,
        UUID studentId,
        Integer marks,
        List<CreateQuestionSubmissionDTO> answers
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            ExamSubmission examSubmission = new ExamSubmission(
                marks == null ? -1 : marks,
                studentId,
                examId
            );

            for (CreateQuestionSubmissionDTO answer : answers) {
                new QuestionSubmission(
                    answer.answer,
                    UUID.fromString(answer.questionId),
                    examSubmission.getId()
                );
            }

            UnitOfWork.getCurrent().commit();
            return examSubmission;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Retrieve all submissions for a given exam.
     *
     * @param examId ID of the exam to retrieve submissions for
     * @return submissions for the specified exam
     */
    public List<ExamSubmission> getSubmissions(UUID examId) {
        try {
            return examSubmissionMapper.findAllForExam(examId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Retrieve the submission for a given user and exam.
     *
     * @param examId ID of the exam
     * @param userId ID of the user
     * @return the submission the user made for the exam
     */
    public ExamSubmission getSubmissionForUser(UUID examId, UUID userId) {
        try {
            return examSubmissionMapper.findByRelationIds(userId, examId);
        } catch (NotFoundException e) {
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update an exam submission.
     *
     * @param examId    ID of the exam
     * @param studentId ID of the student
     * @param marks     Number of marks assigned to the exam submission
     * @return updated ExamSubmission
     */
    public ExamSubmission updateSubmission(
        UUID examId,
        UUID studentId,
        Integer marks
    ) {
        try (
            DBConnection connection = DBConnection.getCurrent();
        ) {
            ExamSubmission examSubmission =
                examSubmissionMapper.findByRelationIds(studentId, examId);

            examSubmission.setMarks(marks);
            UnitOfWork.getCurrent().commit();
            return examSubmission;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }
}
