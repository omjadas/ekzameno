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

/**
 * Data Mapper for Students.
 */
public class StudentMapper extends AbstractUserMapper<Student> {
    /**
     * Retrieve all students for a given subject ID.
     *
     * @param id        ID of the subject to retrieve students for
     * @param forUpdate whether the rows should be locked
     * @return students for the given subject
     * @throws SQLException if unable to retrieve the students
     */
    public List<Student> findAllForSubject(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT users.* FROM users " +
            "JOIN enrolments " +
            "ON users.id = enrolments.user_id " +
            "WHERE enrolments.subject_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Student> students = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Student student = load(rs);
                IdentityMap.getCurrent().put(student.getId(), student);
                students.add(student);
            }

            return students;
        }
    }

    /**
     * Retrieve all students for a given subject ID.
     *
     * @param id ID of the subject to retrieve students for
     * @return students for the given subject
     * @throws SQLException if unable to retrieve the students
     */
    public List<Student> findAllForSubject(UUID id) throws SQLException {
        return findAllForSubject(id, false);
    }

    @Override
    protected Student load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        Student student = (Student) IdentityMap.getCurrent().get(id);
        if (student != null) {
            return student;
        }
        String email = rs.getString("email");
        String name = rs.getString("name");
        String passwordHash = rs.getString("password_hash");
        return new Student(id, email, name, passwordHash);
    }

    @Override
    protected String getType() {
        return Student.TYPE;
    }
}
