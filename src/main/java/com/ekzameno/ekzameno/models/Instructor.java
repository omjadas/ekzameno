package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.SubjectMapper;

public class Instructor extends User {
    public static final String TYPE = "INSTRUCTOR";
    private List<Subject> subjects = null;

    public Instructor(UUID id, String email, String name, String passwordHash) {
        super(id, email, name, passwordHash);
    }

    public Instructor(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }

    public List<Subject> getSubjects() throws SQLException {
        if (subjects == null) {
            return new SubjectMapper().findAllForInstructor(getId());
        } else {
            return subjects;
        }
    }
}
