package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    /**
     * Retrieve all subjects.
     *
     * @return all subjects
     */
    public List<Subject> getSubjects() {
        try (DBConnection connection = DBConnection.getInstance()) {
            return subjectMapper.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retreive subjects for a student.
     *
     * @param id student's id.
     * @return list of subjects for which the user has access.
     */
    public List<Subject> getSubjectsForStudent(UUID id) {
        try (DBConnection connection = DBConnection.getInstance()) {
            return subjectMapper.findAllForStudent(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieve subjects for an instructor.
     *
     * @param id instructor's id.
     * @return list of subjects the instructor teaches.
     */
    public List<Subject> getSubjectsForInstructor(UUID id) {
        try (DBConnection connection = DBConnection.getInstance()) {
            return subjectMapper.findAllForInstructor(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
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
        try (DBConnection connection = DBConnection.getInstance()) {
            Subject subject = new Subject(name, description);

            for (UUID i : instructors) {
                new InstructorSubject(i, subject.getId());
            }

            for (UUID s : students) {
                new Enrolment(s, subject.getId());
            }

            UnitOfWork.getCurrent().commit();
            return subject;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
