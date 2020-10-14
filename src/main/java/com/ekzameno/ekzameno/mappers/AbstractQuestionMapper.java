package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.shared.DBConnection;

/**
 * Abstract Data Mapper for Questions.
 *
 * @param <T> type of the questions
 */
public abstract class AbstractQuestionMapper<T extends Question>
        extends Mapper<T> {
    private static final String tableName = "questions";

    @Override
    public void insert(T question) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, question, marks, type, exam_id) VALUES (?,?,?,?,?)";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, question.getId());
            statement.setString(2, question.getQuestion());
            statement.setInt(3, question.getMarks());
            statement.setString(4, getType());
            statement.setObject(5, question.getExamId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(T question) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET question = ?, marks = ?, exam_id=? WHERE id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, question.getQuestion());
            statement.setInt(2, question.getMarks());
            statement.setObject(3, question.getExamId());
            statement.setObject(4, question.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    protected abstract String getType();
}
