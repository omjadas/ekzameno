package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public abstract class AbstractUserMapper<T extends User> extends Mapper<T> {
    private static final String tableName = "user";

    public void insert(T user) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, email, name, passwordHash, type) VALUES (?,?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            user.setId(UUID.randomUUID());
            statement.setObject(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getName());
            statement.setString(4, user.getPasswordHash());
            statement.setString(5, getType());
            statement.executeUpdate();
            IdentityMap.getInstance().put(user.getId(), user);
        }
    }

    public void update(T user) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET email = ?, name = ?, passwordHash = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPasswordHash());
            statement.setObject(4, user.getId());
            statement.executeUpdate();
        }
    }

    protected String getTableName() {
        return tableName;
    }

    protected abstract String getType();
}
