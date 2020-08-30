package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Administrator;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class AdministratorMapper extends AbstractUserMapper<Administrator> {
    protected Administrator load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("users_id", java.util.UUID.class);
        String email = rs.getString("users_email");
        String name = rs.getString("users_name");
        String passwordHash = rs.getString("users_passwordHash");

        Administrator administrator = new Administrator(
            id,
            email,
            name,
            passwordHash
        );

        IdentityMap.getInstance().put(id, administrator);

        return administrator;
    }

    protected String getType() {
        return Administrator.TYPE;
    }
}
