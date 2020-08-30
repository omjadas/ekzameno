package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Student;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class StudentMapper extends AbstractUserMapper<Student> {
    public List<Student> findAllForSubject(UUID id) throws SQLException {
        String query = "SELECT users.* FROM users " +
            "JOIN instructorSubjects ON users.id = instructorSubjects.userId " +
            "WHERE instructorSubjects.subjectId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Student> students = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Student student = load(rs);
                IdentityMap.getInstance().put(student.getId(), student);
                students.add(student);
            }

            return students;
        }
    }

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
