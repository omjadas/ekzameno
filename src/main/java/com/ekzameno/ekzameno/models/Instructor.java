package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.SubjectMapper;

public class Instructor extends User {
    public static final String TYPE = "INSTRUCTOR";
    private List<Subject> subjects = null;

    /**
     * Create an Instructor with an ID.
     *
     * @param id ID of the Instructor
     * @param email email address of the Instructor
     * @param name name of the Instructor
     * @param passwordHash password hash of the Instructor
     */
    public Instructor(UUID id, String email, String name, String passwordHash) {
        super(id, email, name, passwordHash);
    }

    /**
     * Create an Instructor without an ID (registers as new).
     *
     * @param email email address of the Instructor
     * @param name name of the Instructor
     * @param passwordHash password hash of the Instructor
     */
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
