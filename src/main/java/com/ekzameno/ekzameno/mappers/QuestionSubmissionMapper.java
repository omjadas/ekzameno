package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class QuestionSubmissionMapper extends Mapper<QuestionSubmission> {
    private static final String tableName = "questionSubmissions";
    private static final String columns =
        "questionSubmissions.id AS questionSubmissions_id, " +
        "questionSubmissions.answer AS questionSubmissions_answer";

    public void insert(QuestionSubmission questionSubmission) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answer) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            questionSubmission.setId(UUID.randomUUID());
            statement.setObject(1, questionSubmission.getId());
            statement.setString(2, questionSubmission.getAnswer());
            statement.executeUpdate();
            IdentityMap.getInstance().put(
                questionSubmission.getId(),
                questionSubmission
            );
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
        UUID id = rs.getObject("questionSubmissions_id", java.util.UUID.class);
        String answer = rs.getString("questionSubmissions_answer");

        QuestionSubmission questionSubmission = new QuestionSubmission(
            id,
            answer
        );

        IdentityMap.getInstance().put(id, questionSubmission);

        return questionSubmission;
    }

    protected String getTableName() {
        return tableName;
    }

    protected String getColumns() {
        return columns;
    }
}
