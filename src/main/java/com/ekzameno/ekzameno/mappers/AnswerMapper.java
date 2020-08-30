package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Answer;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class AnswerMapper extends Mapper<Answer> {
    private static final String tableName = "answers";
    private static final String columns = "answers.id AS answers_id, " +
        "answers.answer AS answers_answer";

    public void insert(Answer answer) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answer) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            answer.setId(UUID.randomUUID());
            statement.setObject(1, answer.getId());
            statement.setString(2, answer.getAnswer());
            statement.executeUpdate();
            IdentityMap.getInstance().put(answer.getId(), answer);
        }
    }

    public void update(Answer answer) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET answer = ?, correct = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, answer.getAnswer());
            statement.setBoolean(2, answer.isCorrect());
            statement.setObject(3, answer.getId());
            statement.executeUpdate();
        }
    }

    protected Answer load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("answers_id", java.util.UUID.class);
        String answer = rs.getString("answers_answer");
        boolean correct = rs.getBoolean("answers_correct");
        Answer answerObj = new Answer(id, answer, correct);
        IdentityMap.getInstance().put(id, answerObj);
        return answerObj;
    }

    protected String getTableName() {
        return tableName;
    }

    protected String getColumns() {
        return columns;
    }
}
