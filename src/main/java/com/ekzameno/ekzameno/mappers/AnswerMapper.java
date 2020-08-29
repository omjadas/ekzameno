package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Answer;
import com.ekzameno.ekzameno.shared.DBConnection;

public class AnswerMapper extends Mapper<Answer> {
    private static final String tableName = "answers";

    public void insert(Answer answer) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answers) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, UUID.randomUUID());
            statement.setString(2, answer.getAnswer());
            statement.executeUpdate();
        }
    }

    public void update(Answer answer) throws SQLException {
        String query = "UPDATE " + tableName + " SET answer = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, answer.getAnswer());
            statement.setObject(2, answer.getId());
            statement.executeUpdate();
        }
    }

    protected Answer load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String answer = rs.getString("answer");
        return new Answer(id, answer);
    }

    protected String getTableName() {
        return tableName;
    }
}