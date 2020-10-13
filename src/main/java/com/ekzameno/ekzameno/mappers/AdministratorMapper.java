package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Administrator;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for Administrators.
 */
public class AdministratorMapper extends AbstractUserMapper<Administrator> {
    @Override
    protected Administrator load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        Administrator administrator =
            (Administrator) IdentityMap.getCurrent().get(id);
        if (administrator != null) {
            return administrator;
        }
        String email = rs.getString("email");
        String name = rs.getString("name");
        String passwordHash = rs.getString("password_hash");
        return new Administrator(id, email, name, passwordHash);
    }

    @Override
    protected String getType() {
        return Administrator.TYPE;
    }
}
