package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service to handle subjects.
 */
public class SubjectService {
    private SubjectMapper subjectMapper = new SubjectMapper();

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
        String[] instructors,
        String[] students
    ) {
        try (DBConnection connection = DBConnection.getInstance()) {
            Subject subject = new Subject(name, description);

            // for (String s : instructors) {
            //     new InstructorSubject(UUID.fromString(s), subject.getId());
            // }

            // for (String z : students) {
            //     new Enrolment(UUID.fromString(z), subject.getId());
            // }

            UnitOfWork.getCurrent().commit();
            return subject;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
