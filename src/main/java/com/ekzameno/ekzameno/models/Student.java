package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.proxies.ProxyList;
import com.ekzameno.ekzameno.proxies.SubjectStudentProxyList;

/**
 * Student able to enrol in Subjects and take Exams.
 */
public class Student extends User {
    public static final String TYPE = "STUDENT";
    private ProxyList<Subject> subjects;
    private ProxyList<ExamSubmission> examSubmissions;

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
        this.subjects = new SubjectStudentProxyList(id);
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
        this.subjects = new SubjectStudentProxyList(getId());
    }

    /**
     * Retrieve the Subjects the Student is enrolled in.
     *
     * @return Subjects the Student is enrolled in
     */
    public ProxyList<Subject> getSubjects() {
        return subjects;
    }

    /**
     * Retrieve the ExamSubmissions the Student has made.
     *
     * @return ExamSubmissions the Student has made
     */
    public ProxyList<ExamSubmission> getExamSubmissions() {
        return examSubmissions;
    }
}
