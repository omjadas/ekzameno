package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Answer;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class AnswerMapper extends Mapper<Answer> {
    private static final String tableName = "answers";

    public List<Answer> findAllForQuestion(UUID id) throws SQLException {
        String query = "SELECT * FROM answers WHERE questionId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Answer> answers = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                answers.add(load(rs));
            }

            return answers;
        }
    }

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
        UUID id = rs.getObject("id", java.util.UUID.class);
        String answer = rs.getString("answer");
        boolean correct = rs.getBoolean("correct");
        return new Answer(id, answer, correct);
    }

    protected String getTableName() {
        return tableName;
    }
}
