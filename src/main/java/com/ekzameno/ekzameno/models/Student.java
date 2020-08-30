package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;

public class Student extends User {
    public static final String TYPE = "STUDENT";
    private List<Subject> subjects = null;
    private List<ExamSubmission> examSubmissions = null;

    public Student(UUID id, String email, String name, String passwordHash) {
        super(id, email, name, passwordHash);
    }

    public Student(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }

    public List<Subject> getSubjects() throws SQLException {
        if (subjects == null) {
            return new SubjectMapper().findAllForStudent(getId());
        } else {
            return subjects;
        }
    }

    public List<ExamSubmission> getExamSubmissions() throws SQLException {
        if (examSubmissions == null) {
            return new ExamSubmissionMapper().findAllForStudent(getId());
        } else {
            return examSubmissions;
        }
    }
}
