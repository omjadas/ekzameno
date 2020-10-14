package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Instructor;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for Instructors.
 */
public class InstructorMapper extends AbstractUserMapper<Instructor> {
    /**
     * Retrieve all instructors for a given subject ID.
     *
     * @param id        ID of the subject to retrieve instructors for
     * @param forUpdate whether the rows should be locked
     * @return instructors for the given subject
     * @throws SQLException if unable to retrieve the instructors
     */
    public List<Instructor> findAllForSubject(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT users.* FROM users " +
            "JOIN instructor_subjects " +
            "ON users.id = instructor_subjects.user_id " +
            "WHERE instructor_subjects.subject_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Instructor> instructors = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Instructor instructor = load(rs);
                IdentityMap.getCurrent().put(instructor.getId(), instructor);
                instructors.add(instructor);
            }

            return instructors;
        }
    }

    /**
     * Retrieve all instructors for a given subject ID.
     *
     * @param id ID of the subject to retrieve instructors for
     * @return instructors for the given subject
     * @throws SQLException if unable to retrieve the instructors
     */
    public List<Instructor> findAllForSubject(UUID id) throws SQLException {
        return findAllForSubject(id, false);
    }

    @Override
    protected Instructor load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        Instructor instructor = (Instructor) IdentityMap.getCurrent().get(id);
        if (instructor != null) {
            return instructor;
        }
        String email = rs.getString("email");
        String name = rs.getString("name");
        String passwordHash = rs.getString("password_hash");
        return new Instructor(id, email, name, passwordHash);
    }

    @Override
    protected String getType() {
        return Instructor.TYPE;
    }
}
