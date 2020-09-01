package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for Subjects.
 */
public class SubjectMapper extends Mapper<Subject> {
    private static final String tableName = "subjects";

    /**
     * Retrieve all subjects for a given student ID.
     *
     * @param id ID of the student to retrieve subjects for
     * @return subjects for the given student
     * @throws SQLException if unable to retrieve the subjects
     */
    public List<Subject> findAllForStudent(UUID id) throws SQLException {
        String query = "SELECT subjects.* FROM subjects " +
            "JOIN student_subjects " +
            "ON subjects.id = student_subjects.subject_id " +
            "WHERE student_subjects.user_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Subject> subjects = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Subject subject = load(rs);
                IdentityMap.getInstance().put(subject.getId(), subject);
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
        String query = "SELECT * FROM subjects " +
            "JOIN instructor_subjects ON " +
            "subjects.id = instructor_subjects.subject_id " +
            "WHERE instructor_subjects.user_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Subject> subjects = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Subject subject = load(rs);
                IdentityMap.getInstance().put(subject.getId(), subject);
                subjects.add(subject);
            }

            return subjects;
        }
    }

    @Override
    public void insert(Subject subject) throws SQLException {
        String query = "INSERT INTO " + tableName + " (id, name) VALUES (?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, subject.getId());
            statement.setString(2, subject.getName());
            statement.executeUpdate();
            IdentityMap.getInstance().put(subject.getId(), subject);
        }
    }

    @Override
    public void update(Subject subject) throws SQLException {
        String query = "UPDATE " + tableName + " SET name = ? WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, subject.getName());
            statement.setObject(2, subject.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected Subject load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String name = rs.getString("name");
        return new Subject(id, name);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
