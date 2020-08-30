package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Instructor;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class InstructorMapper extends AbstractUserMapper<Instructor> {
    protected Instructor load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("users_id", java.util.UUID.class);
        String email = rs.getString("users_email");
        String name = rs.getString("users_name");
        String passwordHash = rs.getString("users_passwordHash");
        Instructor instructor = new Instructor(id, email, name, passwordHash);
        IdentityMap.getInstance().put(id, instructor);
        return instructor;
    }

    protected String getType() {
        return Instructor.TYPE;
    }
}
