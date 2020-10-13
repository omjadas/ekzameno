package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for Subjects.
 */
public class SubjectMapper extends Mapper<Subject> {
    private static final String tableName = "subjects";

    public Subject findBySlug(
        String slug,
        boolean forUpdate
    ) throws SQLException, NotFoundException {
        return findByProp("slug", slug, forUpdate);
    }

    /**
     * Find a subject for a given slug.
     *
     * @param slug ID of the subject to find
     * @return subject with the given ID
     * @throws SQLException if unable to retrieve the subject
     */
    public Subject findBySlug(String slug)
        throws SQLException, NotFoundException {
        return findBySlug(slug, false);
    }

    public List<Subject> findAllForStudent(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT subjects.* FROM subjects " +
            "JOIN enrolments " +
            "ON subjects.id = enrolments.subject_id " +
            "WHERE enrolments.user_id = ?" + (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Subject> subjects = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Subject subject = load(rs);
                IdentityMap.getCurrent().put(subject.getId(), subject);
                subjects.add(subject);
            }

            return subjects;
        }
    }

    /**
     * Retrieve all subjects for a given student ID.
     *
     * @param id ID of the student to retrieve subjects for
     * @return subjects for the given student
     * @throws SQLException if unable to retrieve the subjects
     */
    public List<Subject> findAllForStudent(UUID id) throws SQLException {
        return findAllForStudent(id, false);
    }

    public List<Subject> findAllForInstructor(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT * FROM subjects " +
            "JOIN instructor_subjects ON " +
            "subjects.id = instructor_subjects.subject_id " +
            "WHERE instructor_subjects.user_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Subject> subjects = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Subject subject = load(rs);
                IdentityMap.getCurrent().put(subject.getId(), subject);
                subjects.add(subject);
            }

            return subjects;
        }
    }

    /**
     * Retrieve subjects for a given instructor ID.
     *
     * @param id ID of the instructor to retrieve subjects for
     * @return subjects for the given instructor
     * @throws SQLException if unable to retrieve the subjects
     */
    public List<Subject> findAllForInstructor(UUID id) throws SQLException {
        return findAllForInstructor(id, false);
    }

    @Override
    public void insert(Subject subject) throws SQLException {
        String query = "INSERT INTO " +
            tableName +
            " (id, slug, name, description) VALUES (?,?,?,?)";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, subject.getId());
            statement.setString(2, subject.getSlug());
            statement.setString(3, subject.getName());
            statement.setString(4, subject.getDescription());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Subject subject) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET name = ?, description = ? WHERE id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, subject.getName());
            statement.setString(2, subject.getDescription());
            statement.setObject(3, subject.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected Subject load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        Subject subject = (Subject) IdentityMap.getCurrent().get(id);
        if (subject != null) {
            return subject;
        }
        String name = rs.getString("name");
        String description = rs.getString("description");
        String slug = rs.getString("slug");
        return new Subject(id, name, description, slug);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
