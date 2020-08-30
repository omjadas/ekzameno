package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.ExamSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;

public class ExamSubmissionMapper extends Mapper<ExamSubmission> {
    private static final String tableName = "examSubmissions";

    public void insert(ExamSubmission examSubmission) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, marks) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, UUID.randomUUID());
            statement.setInt(2, examSubmission.getMarks());
            statement.executeUpdate();
        }
    }

    public void update(ExamSubmission examSubmission) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET marks = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, examSubmission.getMarks());
            statement.setObject(2, examSubmission.getId());
            statement.executeUpdate();
        }
    }

    protected ExamSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        int marks = rs.getInt("marks");
        return new ExamSubmission(id, marks);
    }

    protected String getTableName() {
        return tableName;
    }
}
