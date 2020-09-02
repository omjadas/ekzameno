package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Enrolment;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for Enrolments.
 */
public class EnrolmentMapper extends Mapper<Enrolment> {
    private static final String tableName = "enrolments";

    /**
     * Retrieve the Enrolment with the given relation IDs.
     *
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     * @return the Enrolment with the specified relation IDs
     * @throws SQLException if unable to retrieve the Enrolment
     */
    public Enrolment findByRelationIds(
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
            Enrolment enrolment = load(rs);
            IdentityMap.getInstance().put(
                enrolment.getId(),
                enrolment
            );
            return enrolment;
        }
    }

    @Override
    public void insert(
        Enrolment enrolment
    ) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, user_id, subject_id) VALUES (?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, enrolment.getId());
            statement.setObject(2, enrolment.getStudentId());
            statement.setObject(3, enrolment.getSubjectId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(
        Enrolment enrolment
    ) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET user_id = ?, subject_id = ? WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, enrolment.getStudentId());
            statement.setObject(1, enrolment.getSubjectId());
            statement.setObject(1, enrolment.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Delete the Enrolment with the given relation IDs.
     *
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     * @throws SQLException if unable to delete the Enrolment
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
    protected Enrolment load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        UUID studentId = rs.getObject("user_id", java.util.UUID.class);
        UUID subjectId = rs.getObject("subject_id", java.util.UUID.class);
        return new Enrolment(id, studentId, subjectId);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}