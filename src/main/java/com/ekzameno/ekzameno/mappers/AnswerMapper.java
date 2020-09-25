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

/**
 * Data Mapper for Answers.
 */
public class AnswerMapper extends Mapper<Answer> {
    private static final String tableName = "answers";

    /**
     * Retrieve all answers for a given question ID.
     *
     * @param id ID of the question to retrieve answers for
     * @return answers for the given question
     * @throws SQLException if unable to retrieve the answers
     */
    public List<Answer> findAllForQuestion(UUID id) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE question_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Answer> answers = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Answer answer = load(rs);
                IdentityMap.getInstance().put(answer.getId(), answer);
                answers.add(answer);
            }

            return answers;
        }
    }

    @Override
    public void insert(Answer answer) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answer, correct, question_id) VALUES (?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, answer.getId());
            statement.setString(2, answer.getAnswer());
            statement.setBoolean(3, answer.isCorrect());
            statement.setObject(4, answer.getQuestionId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Answer answer) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET answer = ?, correct = ?, question_id = ? WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, answer.getAnswer());
            statement.setBoolean(2, answer.isCorrect());
            statement.setObject(3, answer.getQuestionId());
            statement.setObject(4, answer.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected Answer load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String answer = rs.getString("answer");
        boolean correct = rs.getBoolean("correct");
        UUID questionId = rs.getObject("question_id", java.util.UUID.class);
        return new Answer(id, answer, correct, questionId);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
