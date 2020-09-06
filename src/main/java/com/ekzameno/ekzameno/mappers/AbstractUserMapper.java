package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.shared.DBConnection;

/**
 * Abstract Data Mapper for Users.
 *
 * @param <T> type of the users
 */
public abstract class AbstractUserMapper<T extends User> extends Mapper<T> {
    private static final String tableName = "users";

    /**
     * Find a user by their email address.
     *
     * @param email email address of the user to find
     * @return the user with the specified email address
     * @throws SQLException if unable to retrieve the user
     */
    public User findByEmail(String email) throws SQLException {
        return findByProp("email", email);
    }

    @Override
    public void insert(T user) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, email, name, password_hash, type) VALUES (?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getName());
            statement.setString(4, user.getPasswordHash());
            statement.setString(5, getType());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(T user) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET email = ?, name = ?, password_hash = ? WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPasswordHash());
            statement.setObject(4, user.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    protected abstract String getType();
}
