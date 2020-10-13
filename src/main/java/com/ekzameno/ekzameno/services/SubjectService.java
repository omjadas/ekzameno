package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.mappers.EnrolmentMapper;
import com.ekzameno.ekzameno.mappers.InstructorSubjectMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.models.Enrolment;
import com.ekzameno.ekzameno.models.InstructorSubject;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service to handle subjects.
 */
public class SubjectService {
    private final SubjectMapper subjectMapper = new SubjectMapper();
    private final EnrolmentMapper enrolmentMapper = new EnrolmentMapper();
    private final InstructorSubjectMapper instructorSubjectMapper =
        new InstructorSubjectMapper();

    /**
     * Retrieve all subjects.
     *
     * @return all subjects
     */
    public List<Subject> getSubjects() {
        try (DBConnection connection = DBConnection.getCurrent()) {
            return subjectMapper.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Fetches a subject for a given slug.
     *
     * @param slug subject's slug
     * @return subject
     */
    public Subject getSubject(String slug)
        throws NotFoundException, InternalServerErrorException {
        try (DBConnection connection = DBConnection.getCurrent()) {
            return subjectMapper.findBySlug(slug);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Retrieve subjects for an instructor.
     *
     * @param id instructor's id.
     * @return list of subjects the instructor teaches.
     */
    public List<Subject> getSubjectsForInstructor(UUID id) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            return subjectMapper.findAllForInstructor(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieve subjects for a student.
     *
     * @param id student's id.
     * @return list of subjects for which the user has access.
     */
    public List<Subject> getSubjectsForStudent(UUID id) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            return subjectMapper.findAllForStudent(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Adds given instructor to given subject.
     *
     * @param subjectId subject's id
     * @param instructorId instructor's id
     * @throws NotFoundException not found exception
     * @throws InternalServerErrorException internal error exception
     */
    public void addInstructorToSubject(UUID subjectId, UUID instructorId) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            new InstructorSubject(instructorId, subjectId);
            UnitOfWork.getCurrent().commit();
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if ("23503".equals(e.getSQLState())) {
                throw new NotFoundException();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Adds given student to given subject.
     *
     * @param subjectId subject's id
     * @param studentId student's id
     * @throws NotFoundException not found exception
     * @throws InternalServerErrorException internal error exception
     */
    public void addStudentToSubject(UUID subjectId, UUID studentId) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            new Enrolment(studentId, subjectId);
            UnitOfWork.getCurrent().commit();
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if ("23503".equals(e.getSQLState())) {
                throw new NotFoundException();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Deletes given instructor from given subject.
     *
     * @param subjectId subject's id
     * @param instructorId instructor's id
     */
    public void deleteInstructorFromSubject(
        UUID subjectId,
        UUID instructorId
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            instructorSubjectMapper.deleteByRelationIds(
                instructorId,
                subjectId
            );
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
     * Deletes given student from given subject.
     *
     * @param subjectId subject's id
     * @param studentId student's id
     */
    public void deleteStudentFromSubject(
        UUID subjectId,
        UUID studentId
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            enrolmentMapper.deleteByRelationIds(
                studentId,
                subjectId
            );
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
     * Create a new subject.
     *
     * @param name name of the subject to create
     * @param description description of the subject to create
     * @param instructors instructors assigned to the new subject
     * @param students students enrolled in the subject
     * @return the new subject
     */
    public Subject createSubject(
        String name,
        String description,
        UUID[] instructors,
        UUID[] students
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            Subject subject = new Subject(name, description);

            for (UUID i : instructors) {
                new InstructorSubject(i, subject.getId());
            }

            for (UUID s : students) {
                new Enrolment(s, subject.getId());
            }

            UnitOfWork.getCurrent().commit();
            return subject;
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
     * Updates a subject.
     *
     * @param name new name of the subject
     * @param description new description of the subject
     * @param subjectId subject id
     * @return returns the updated subject
     */
    public Subject updateSubject(
        String name,
        String description,
        UUID subjectId
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            Subject subject = subjectMapper.findById(subjectId);
            subject.setName(name);
            subject.setDescription(description);
            UnitOfWork.getCurrent().commit();
            return subject;
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
