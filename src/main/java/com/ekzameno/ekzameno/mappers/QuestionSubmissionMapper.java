package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class QuestionSubmissionMapper extends Mapper<QuestionSubmission> {
    private static final String tableName = "questionSubmissions";

    public List<QuestionSubmission> findAllForExamSubmission(UUID id)
            throws SQLException {
        String query = "SELECT * FROM questionSubmissions " +
            "WHERE examSubmissionId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<QuestionSubmission> questionSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);

                IdentityMap.getInstance().put(
                    questionSubmission.getId(),
                    questionSubmission
                );

                questionSubmissions.add(load(rs));
            }

            return questionSubmissions;
        }
    }

    public List<QuestionSubmission> findAllForQuestion(UUID id)
            throws SQLException {
        String query = "SELECT questionSubmissions.* " +
            "FROM questionSubmissions " +
            "WHERE questionSubmissions.questionId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<QuestionSubmission> questionSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);

                IdentityMap.getInstance().put(
                    questionSubmission.getId(),
                    questionSubmission
                );

                questionSubmissions.add(questionSubmission);
            }

            return questionSubmissions;
        }
        }

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
        UUID id = rs.getObject("id", java.util.UUID.class);
        String answer = rs.getString("answer");
        return new QuestionSubmission(id, answer);
    }

    protected String getTableName() {
        return tableName;
    }
}
