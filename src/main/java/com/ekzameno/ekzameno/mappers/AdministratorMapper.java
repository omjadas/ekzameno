package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Administrator;

public class AdministratorMapper extends AbstractUserMapper<Administrator> {
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
