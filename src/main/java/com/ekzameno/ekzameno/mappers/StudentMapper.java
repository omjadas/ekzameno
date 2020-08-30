package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Student;

public class StudentMapper extends AbstractUserMapper<Student> {
    protected Student load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String email = rs.getString("email");
        String name = rs.getString("name");
        String passwordHash = rs.getString("passwordHash");
        return new Student(id, email, name, passwordHash);
    }

    protected String getType() {
        return Student.TYPE;
    }
}
