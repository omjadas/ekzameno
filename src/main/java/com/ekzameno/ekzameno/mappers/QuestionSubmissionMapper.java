package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;

public class QuestionSubmissionMapper extends Mapper<QuestionSubmission> {
    private static final String tableName = "questionSubmissions";

    public void insert(QuestionSubmission questionSubmission) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answer) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, UUID.randomUUID());
            statement.setString(2, questionSubmission.getAnswer());
            statement.executeUpdate();
        }
    }

    public void update(QuestionSubmission questionSubmission) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET answer = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, questionSubmission.getAnswer());
            statement.setObject(2, questionSubmission.getId());
            statement.executeUpdate();
        }
    }

    protected QuestionSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String answer = rs.getString("answer");
        return new QuestionSubmission(id, answer);
    }

    protected String getTableName() {
        return tableName;
    }
}
