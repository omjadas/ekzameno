package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class SubjectMapper extends Mapper<Subject> {
    private static final String tableName = "subjects";
    private static final String columns = "subjects.id as subjects_id, " +
        "subjects.name as subjects_name";

    public void insert(Subject subject) throws SQLException {
        String query = "INSERT INTO " + tableName + " (id, name) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            subject.setId(UUID.randomUUID());
            statement.setObject(1, subject.getId());
            statement.setString(2, subject.getName());
            statement.executeUpdate();
            IdentityMap.getInstance().put(subject.getId(), subject);
        }
    }

    public void update(Subject subject) throws SQLException {
        String query = "UPDATE " + tableName + " SET name = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, subject.getName());
            statement.setObject(2, subject.getId());
            statement.executeUpdate();
        }
    }

    protected Subject load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("subjects_id", java.util.UUID.class);
        String name = rs.getString("subjects_name");
        Subject subject = new Subject(id, name);
        IdentityMap.getInstance().put(id, subject);
        return subject;
    }

    protected String getTableName() {
        return tableName;
    }

    protected String getColumns() {
        return columns;
    }
}
