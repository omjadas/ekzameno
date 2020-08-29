package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Administrator;
import com.ekzameno.ekzameno.shared.DBConnection;

public class AdministratorMapper extends AbstractUserMapper<Administrator> {
    public void insert(Administrator administrator) throws SQLException {
        String query = "INSERT INTO " + getTableName() +
            " (id, name, passwordHash, type) VALUES (?,?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, UUID.randomUUID());
            statement.setString(2, administrator.getName());
            statement.setString(3, administrator.getPasswordHash());
            statement.setString(4, Administrator.TYPE);
            statement.executeUpdate();
        }
    }

    protected Administrator load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String email = rs.getString("email");
        String name = rs.getString("name");
        String passwordHash = rs.getString("passwordHash");
        return new Administrator(id, email, name, passwordHash);
    }

    protected String getType() {
        return Administrator.TYPE;
    }
}
