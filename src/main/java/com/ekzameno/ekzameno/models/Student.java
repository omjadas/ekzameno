package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;

/**
 * Student able to enrol in Subjects and take Exams.
 */
public class Student extends User {
    public static final String TYPE = "STUDENT";
    private List<Subject> subjects = null;
    private List<ExamSubmission> examSubmissions = null;

    /**
     * Create a Student with an ID.
     *
     * @param id           ID of the Student
     * @param email        email address of the student
     * @param name         name of the student
     * @param passwordHash password hash of the student
     */
    public Student(UUID id, String email, String name, String passwordHash) {
        super(id, email, name, passwordHash);
    }

    /**
     * Create a Student without an ID (registers as new).
     *
     * @param email        email address of the student
     * @param name         name of the student
     * @param passwordHash password hash of the student
     */
    public Student(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }

    /**
     * Retrieve the Subjects the Student is enrolled in.
     *
     * @return Subjects the Student is enrolled in
     * @throws SQLException if unable to retrieve the Subjects
     */
    public List<Subject> getSubjects() throws SQLException {
        if (subjects == null) {
            return new SubjectMapper().findAllForStudent(getId());
        } else {
            return subjects;
        }
    }

    /**
     * Retrieve the ExamSubmissions the Student has made.
     *
     * @return ExamSubmissions the Student has made
     * @throws SQLException if unable to retrieve the ExamSubmissions
     */
    public List<ExamSubmission> getExamSubmissions() throws SQLException {
        if (examSubmissions == null) {
            return new ExamSubmissionMapper().findAllForStudent(getId());
        } else {
            return examSubmissions;
        }
    }
}
