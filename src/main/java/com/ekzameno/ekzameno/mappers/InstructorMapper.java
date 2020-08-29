package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Instructor;

public class InstructorMapper extends AbstractUserMapper<Instructor> {
    protected Instructor load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String name = rs.getString("name");
        String passwordHash = rs.getString("passwordHash");
        return new Instructor(id, name, passwordHash);
    }

    protected String getType() {
        return Instructor.TYPE;
    }
}
