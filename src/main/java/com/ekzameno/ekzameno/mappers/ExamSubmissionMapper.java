package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.ExamSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class ExamSubmissionMapper extends Mapper<ExamSubmission> {
    private static final String tableName = "examSubmissions";

    public List<ExamSubmission> findAllForExam(UUID id) throws SQLException {
        String query = "SELECT * FROM examSubmissions WHERE examId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<ExamSubmission> examSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                examSubmissions.add(load(rs));
            }

            return examSubmissions;
        }
    }

    public List<ExamSubmission> findAllForStudent(UUID id) throws SQLException {
        String query = "SELECT examSubmissions.* FROM examSubmissions " +
            "WHERE examSubmissions.userId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<ExamSubmission> examSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                examSubmissions.add(load(rs));
            }

            return examSubmissions;
        }
    }

    public void insert(ExamSubmission examSubmission) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, marks) VALUES (?,?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            examSubmission.setId(UUID.randomUUID());
            statement.setObject(1, examSubmission.getId());
            statement.setInt(2, examSubmission.getMarks());
            statement.executeUpdate();
            IdentityMap.getInstance().put(
                examSubmission.getId(),
                examSubmission
            );
        }
    }

    public void update(ExamSubmission examSubmission) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET marks = ? WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, examSubmission.getMarks());
            statement.setObject(2, examSubmission.getId());
            statement.executeUpdate();
        }
    }

    protected ExamSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        int marks = rs.getInt("marks");
        return new ExamSubmission(id, marks);
    }

    protected String getTableName() {
        return tableName;
    }
}
