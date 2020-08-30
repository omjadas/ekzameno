package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.ExamSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class ExamSubmissionMapper extends Mapper<ExamSubmission> {
    private static final String tableName = "examSubmissions";
    private static final String columns =
        "examSubmissions.id AS examSubmissions_id, " +
        "examSubmissions.marks AS examSubmissions_marks";

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
        UUID id = rs.getObject("examSubmissions_id", java.util.UUID.class);
        int marks = rs.getInt("examSubmissions_marks");
        ExamSubmission examSubmission = new ExamSubmission(id, marks);
        IdentityMap.getInstance().put(id, examSubmission);
        return examSubmission;
    }

    protected String getTableName() {
        return tableName;
    }

    protected String getColumns() {
        return columns;
    }
}
