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

import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class ExamMapper extends Mapper<Exam> {
    private static final String tableName = "exams";

    public List<Exam> findAllForSubject(UUID id) throws SQLException {
        String query = "SELECT * FROM exams WHERE subjectId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Exam> exams = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                exams.add(load(rs));
            }

            return exams;
        }
    }

    public void insert(Exam exam) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, name, publishDate, closeDate) " +
            "VALUES (?,?,?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            exam.setId(UUID.randomUUID());
            statement.setObject(1, exam.getId());
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
            IdentityMap.getInstance().put(exam.getId(), exam);
        }
    }

    public void update(Exam exam) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET name = ?, publishDate = ?, closeDate = ? " +
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

    protected Exam load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String name = rs.getString("name");
        Date publishDate = rs.getTimestamp("publishDate");
        Date closeDate = rs.getTimestamp("closeDate");
        return new Exam(id, name, publishDate, closeDate);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
