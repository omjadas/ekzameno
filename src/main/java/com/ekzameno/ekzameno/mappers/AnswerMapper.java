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

public class AnswerMapper extends Mapper<Answer> {
    public Answer find(UUID id) throws SQLException {
        String query = "SELECT * FROM answers WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String answer = rs.getString("answer");
            return new Answer(id, answer);
        }
    }

    public List<Answer> findAll() throws SQLException {
        String query = "SELECT * FROM answers";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            ResultSet rs = statement.executeQuery();
            List<Answer> answers = new ArrayList<>();

            while (rs.next()) {
                UUID id = rs.getObject("id", java.util.UUID.class);
                String answer = rs.getString("answer");
                answers.add(new Answer(id, answer));
            }

            return answers;
        }
    }

    public void insert(Answer answer) throws SQLException {
        String query = "INSERT INTO answers (id, answers) VALUES (?,?)";

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
        String query = "UPDATE answers SET answer = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, answer.getAnswer());
            statement.setObject(2, answer.getId());
            statement.executeUpdate();
        }
    }

    public void delete(Answer answer) throws SQLException {
        String query = "DELETE FROM answers WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, answer.getId());
            statement.executeUpdate();
        }
    }
}