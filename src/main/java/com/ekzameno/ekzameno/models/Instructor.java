package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.proxies.ProxyList;
import com.ekzameno.ekzameno.proxies.SubjectInstructorProxyList;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Instructor for a Subject.
 */
public class Instructor extends User {
    public static final String TYPE = "INSTRUCTOR";
    @JsonIgnore
    private ProxyList<Subject> subjects;

    /**
     * Create an Instructor with an ID.
     *
     * @param id           ID of the Instructor
     * @param email        email address of the Instructor
     * @param name         name of the Instructor
     * @param passwordHash password hash of the Instructor
     */
    public Instructor(UUID id, String email, String name, String passwordHash) {
        super(id, email, name, passwordHash, TYPE);
        subjects = new SubjectInstructorProxyList(id);
    }

    /**
     * Create an Instructor without an ID (registers as new).
     *
     * @param email        email address of the Instructor
     * @param name         name of the Instructor
     * @param passwordHash password hash of the Instructor
     */
    public Instructor(String email, String name, String passwordHash) {
        super(email, name, passwordHash, TYPE);
        subjects = new SubjectInstructorProxyList(getId());
    }

    /**
     * Retrieve the Subjects that the Instructor teaches.
     *
     * @return Subjects the Instructor teaches
     * @throws SQLException if unable to retrieve the Subjects
     */
    public ProxyList<Subject> getSubjects() throws SQLException {
        return subjects;
    }
}
