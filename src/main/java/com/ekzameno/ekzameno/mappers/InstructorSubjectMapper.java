package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.models.InstructorSubject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for InstructorSubjects.
 */
public class InstructorSubjectMapper extends Mapper<InstructorSubject> {
    private static final String tableName = "instructor_subjects";

    /**
     * Retrieve the InstructorSubject with the given relation IDs.
     *
     * @param instructorId ID of the instructor
     * @param subjectId    ID of the subject
     * @param forUpdate    whether the row should be locked
     * @return the InstructorSubject with the given relation IDs
     * @throws SQLException if unable to retrieve the InstructorSubject
     */
    public InstructorSubject findByRelationIds(
        UUID instructorId,
        UUID subjectId,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName +
            " WHERE user_id = ? AND subject_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, instructorId);
            statement.setObject(2, subjectId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                InstructorSubject instructorSubject = load(rs);
                IdentityMap.getCurrent().put(
                    instructorSubject.getId(),
                    instructorSubject
                );
                return instructorSubject;
            } else {
                throw new NotFoundException();
            }
        }
    }

    /**
     * Retrieve the InstructorSubject with the given relation IDs.
     *
     * @param instructorId ID of the instructor
     * @param subjectId    ID of the subject
     * @return the InstructorSubject with the given relation IDs
     * @throws SQLException if unable to retrieve the InstructorSubject
     */
    public InstructorSubject findByRelationIds(
        UUID instructorId,
        UUID subjectId
    ) throws SQLException {
        return findByRelationIds(instructorId, subjectId, false);
    }

    @Override
    public void insert(
        InstructorSubject instructorSubject
    ) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, user_id, subject_id) VALUES (?,?,?)";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, instructorSubject.getId());
            statement.setObject(2, instructorSubject.getInstructorId());
            statement.setObject(3, instructorSubject.getSubjectId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(
        InstructorSubject instructorSubject
    ) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET user_id = ?, subject_id = ? WHERE id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, instructorSubject.getInstructorId());
            statement.setObject(1, instructorSubject.getSubjectId());
            statement.setObject(1, instructorSubject.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Delete the InstructorSubject with the given relation IDs.
     *
     * @param instructorId ID of the instructor
     * @param subjectId    ID of the subject
     * @throws SQLException if unable to delete the InstructorSubject
     */
    public void deleteByRelationIds(
        UUID instructorId,
        UUID subjectId
    ) throws SQLException {
        String query = "DELETE FROM " + tableName +
            " WHERE user_id = ? AND subject_id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, instructorId);
            statement.setObject(2, subjectId);
            statement.executeUpdate();
        }
    }

    @Override
    protected InstructorSubject load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        InstructorSubject instructorSubject = (InstructorSubject) IdentityMap
            .getCurrent()
            .get(id);
        if (instructorSubject != null) {
            return instructorSubject;
        }
        UUID instructorId = rs.getObject("user_id", java.util.UUID.class);
        UUID subjectId = rs.getObject("subject_id", java.util.UUID.class);
        return new InstructorSubject(id, instructorId, subjectId);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
