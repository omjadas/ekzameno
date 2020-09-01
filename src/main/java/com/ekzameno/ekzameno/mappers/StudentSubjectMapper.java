package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.StudentSubject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for StudentSubjects.
 */
public class StudentSubjectMapper extends Mapper<StudentSubject> {
    private static final String tableName = "student_subjects";

    /**
     * Retrieve the StudentSubject with the given relation IDs.
     *
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     * @return the StudentSubject with the specified relation IDs
     * @throws SQLException if unable to retrieve the StudentSubject
     */
    public StudentSubject findByRelationIds(
        UUID studentId,
        UUID subjectId
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName +
            " WHERE user_id = ? AND subject_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, studentId);
            statement.setObject(2, subjectId);
            ResultSet rs = statement.executeQuery();
            StudentSubject studentSubject = load(rs);
            IdentityMap.getInstance().put(
                studentSubject.getId(),
                studentSubject
            );
            return studentSubject;
        }
    }

    @Override
    public void insert(
        StudentSubject studentSubject
    ) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, user_id, subject_id) VALUES (?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, studentSubject.getId());
            statement.setObject(2, studentSubject.getStudentId());
            statement.setObject(3, studentSubject.getSubjectId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(
        StudentSubject studentSubject
    ) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET user_id = ?, subject_id = ? WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, studentSubject.getStudentId());
            statement.setObject(1, studentSubject.getSubjectId());
            statement.setObject(1, studentSubject.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Delete the StudentSubject with the given relation IDs.
     *
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     * @throws SQLException if unable to delete the StudentSubject
     */
    public void deleteByRelationIds(
        UUID studentId,
        UUID subjectId
    ) throws SQLException {
        String query = "DELETE FROM " + tableName +
            " WHERE user_id = ? AND subject_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, studentId);
            statement.setObject(2, subjectId);
            statement.executeUpdate();
        }
    }

    @Override
    protected StudentSubject load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        UUID studentId = rs.getObject("user_id", java.util.UUID.class);
        UUID subjectId = rs.getObject("subject_id", java.util.UUID.class);
        return new StudentSubject(id, studentId, subjectId);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
