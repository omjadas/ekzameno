package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Option;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for Options.
 */
public class OptionMapper extends Mapper<Option> {
    private static final String tableName = "options";

    public List<Option> findAllForQuestion(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE question_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Option> options = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Option option = load(rs);
                IdentityMap.getCurrent().put(option.getId(), option);
                options.add(option);
            }

            return options;
        }
    }

    /**
     * Retrieve all options for a given question ID.
     *
     * @param id ID of the question to retrieve options for
     * @return options for the given question
     * @throws SQLException if unable to retrieve the options
     */
    public List<Option> findAllForQuestion(UUID id) throws SQLException {
        return findAllForQuestion(id, false);
    }

    @Override
    public void insert(Option option) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answer, correct, question_id) VALUES (?,?,?,?)";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, option.getId());
            statement.setString(2, option.getAnswer());
            statement.setBoolean(3, option.isCorrect());
            statement.setObject(4, option.getQuestionId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Option option) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET answer = ?, correct = ?, question_id = ? WHERE id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, option.getAnswer());
            statement.setBoolean(2, option.isCorrect());
            statement.setObject(3, option.getQuestionId());
            statement.setObject(4, option.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected Option load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        Option option = (Option) IdentityMap.getCurrent().get(id);
        if (option != null) {
            return option;
        }
        String answer = rs.getString("answer");
        boolean correct = rs.getBoolean("correct");
        UUID questionId = rs.getObject("question_id", java.util.UUID.class);
        return new Option(id, answer, correct, questionId);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
