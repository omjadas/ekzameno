package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Student;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class StudentMapper extends AbstractUserMapper<Student> {
    protected Student load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("users_id", java.util.UUID.class);
        String email = rs.getString("users_email");
        String name = rs.getString("users_name");
        String passwordHash = rs.getString("users_passwordHash");
        Student student = new Student(id, email, name, passwordHash);
        IdentityMap.getInstance().put(id, student);
        return student;
    }

    protected String getType() {
        return Student.TYPE;
    }
}
