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
            "JOIN studentSubjects ON subjects.id = studentSubjects.subjectId " +
            "WHERE studentSubjects.userId = ?";

        try (
            Connection connection = DBConnection.getConnection();
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
            "JOIN instructorSubjects ON " +
            "subjects.id = instructorSubjects.subjectId " +
            "WHERE instructorSubjects.userId = ?";

        try (
            Connection connection = DBConnection.getConnection();
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

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            subject.setId(UUID.randomUUID());
            statement.setObject(1, subject.getId());
            statement.setString(2, subject.getName());
            statement.executeUpdate();
            IdentityMap.getInstance().put(subject.getId(), subject);
        }
    }

    @Override
    public void update(Subject subject) throws SQLException {
        String query = "UPDATE " + tableName + " SET name = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
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
