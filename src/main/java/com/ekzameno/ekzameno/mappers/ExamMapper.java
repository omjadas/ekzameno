package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.DBConnection;
import com.ekzameno.ekzameno.models.Exam;

public class ExamMapper extends Mapper<Exam> {
    public Exam find(UUID id) throws SQLException {
        String query = "SELECT * FROM exams WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String name = rs.getString("name");
            Date publishDate = rs.getDate("publishDate");
            Date closeDate = rs.getDate("closeDate");
            return new Exam(id, name, publishDate, closeDate);
        }
    }

    public List<Exam> findAll() throws SQLException {
        String query = "SELECT * FROM exams";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            ResultSet rs = statement.executeQuery();
            List<Exam> exams = new ArrayList<>();

            while (rs.next()) {
                UUID id = rs.getObject("id", java.util.UUID.class);
                String name = rs.getString("name");
                Date publishDate = rs.getTimestamp("publishDate");
                Date closeDate = rs.getTimestamp("closeDate");
                exams.add(new Exam(id, name, publishDate, closeDate));
            }

            return exams;
        }
    }

    public void insert(Exam exam) throws SQLException {
        String query = "INSERT INTO exams (id, name, publishDate, closeDate) " +
            "VALUES (?,?,?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, UUID.randomUUID());
            statement.setString(2, exam.getName());
            statement.setTimestamp(
                3,
                new Timestamp(exam.getPublishDate().getTime())
            );
            statement.setTimestamp(
                4,
                new Timestamp(exam.getCloseDate().getTime())
            );
            statement.executeUpdate();
        }
    }

    public void update(Exam exam) throws SQLException {
        String query = "UPDATE exams " +
            "SET name = ?, publishDate = ?, closeDate = ? " +
            "WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, exam.getName());
            statement.setTimestamp(
                2,
                new Timestamp(exam.getPublishDate().getTime())
            );
            statement.setTimestamp(
                3,
                new Timestamp(exam.getCloseDate().getTime())
            );
            statement.setObject(4, exam.getId());
            statement.executeUpdate();
        }
    }

    public void delete(Exam exam) throws SQLException {
        String query = "DELETE FROM exams WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, exam.getId());
            statement.executeUpdate();
        }
    }
}
