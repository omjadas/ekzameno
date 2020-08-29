package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.shared.DBConnection;

public class SubjectMapper extends Mapper<Subject> {
    public Subject find(UUID id) throws SQLException {
        String query = "SELECT * FROM exams WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, id);
            try (ResultSet rs = statement.executeQuery();) {
                rs.next();
                String name = rs.getString("name");
                return new Subject(id, name);
            }
        }
    }

    public List<Subject> findAll() throws SQLException {
        String query = "SELECT * FROM subjects";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
        ) {
            List<Subject> subjects = new ArrayList<>();

            while (rs.next()) {
                UUID id = rs.getObject("id", java.util.UUID.class);
                String name = rs.getString("name");
                subjects.add(new Subject(id, name));
            }

            return subjects;
        }
    }

    public void insert(Subject subject) throws SQLException {
        String query = "INSERT INTO subjects (id, name) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, UUID.randomUUID());
            statement.setString(2, subject.getName());
            statement.executeUpdate();
        }
    }

    public void update(Subject subject) throws SQLException {
        String query = "UPDATE subjects SET name = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, subject.getName());
            statement.setObject(2, subject.getId());
            statement.executeUpdate();
        }
    }

    public void delete(Subject subject) throws SQLException {
        String query = "DELETE FROM subjects WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, subject.getId());
            statement.executeUpdate();
        }
    }
}
