package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public abstract class AbstractQuestionMapper<T extends Question>
        extends Mapper<T> {
    private static final String tableName = "question";

    @Override
    public void insert(T obj) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, question, marks, type) VALUES (?,?,?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            obj.setId(UUID.randomUUID());
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getQuestion());
            statement.setInt(3, obj.getMarks());
            statement.setString(4, getType());
            statement.executeUpdate();
            IdentityMap.getInstance().put(obj.getId(), obj);
        }
    }

    @Override
    public void update(T obj) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET question = ?, marks = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, obj.getQuestion());
            statement.setInt(2, obj.getMarks());
            statement.setObject(3, obj.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    protected abstract String getType();
}
