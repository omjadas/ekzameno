package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.mappers.InstructorMapper;
import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Subjects that Instructors teach and Students enrol in.
 */
public class Subject extends Model {
    private String name;
    private List<Instructor> instructors = null;
    private List<Student> students = null;
    private List<Exam> exams = null;

    /**
     * Create a Subject with an ID.
     *
     * @param id   ID of the Subject
     * @param name name of the Subject
     */
    public Subject(UUID id, String name) {
        super(id);
        this.name = name;
    }

    /**
     * Create a Subject without an ID (registers as new).
     *
     * @param name name of the Subject
     */
    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Retrieve the Instructors for the Subject.
     *
     * @return Instructors for the Subject
     * @throws SQLException if unable to retrieve the Instructors
     */
    public List<Instructor> getInstructors() throws SQLException {
        if (instructors == null) {
            return new InstructorMapper().findAllForSubject(getId());
        } else {
            return instructors;
        }
    }

    /**
     * Retrieve the Students for the Subject.
     *
     * @return Students enrolled in the Subject
     * @throws SQLException if unable to retrieve the Students
     */
    public List<Student> getStudents() throws SQLException {
        if (students == null) {
            return new StudentMapper().findAllForSubject(getId());
        } else {
            return students;
        }
    }

    /**
     * Retrieve the Exams for the Subject.
     *
     * @return Exams created for the Subject
     * @throws SQLException if unable to retrieve the Exams
     */
    public List<Exam> getExams() throws SQLException {
        if (exams == null) {
            return new ExamMapper().findAllForSubject(getId());
        } else {
            return exams;
        }
    }

    /**
     * Set the name of the Subject (registers the Subject as dirty).
     *
     * @param name name of the Subject
     */
    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
