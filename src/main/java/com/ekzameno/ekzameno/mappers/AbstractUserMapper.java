package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.shared.DBConnection;

public abstract class AbstractUserMapper<T extends User> extends Mapper<T> {
    private static final String tableName = "user";

    public void insert(T obj) throws SQLException {
        String query = "INSERT INTO " + getTableName() +
            " (id, name, passwordHash, type) VALUES (?,?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, UUID.randomUUID());
            statement.setString(2, obj.getName());
            statement.setString(3, obj.getPasswordHash());
            statement.setString(4, getType());
            statement.executeUpdate();
        }
    }

    public void update(T obj) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET name = ?, passwordHash = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getPasswordHash());
            statement.setObject(3, obj.getId());
            statement.executeUpdate();
        }
    }

    protected String getTableName() {
        return tableName;
    }

    protected abstract String getType();
}
