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

/**
 * Data Mapper for ExamSubmissions.
 */
public class ExamSubmissionMapper extends Mapper<ExamSubmission> {
    private static final String tableName = "examSubmissions";

    /**
     * Retrieve all exam submissions for a given exam ID.
     *
     * @param id ID of the exam to retrieve submissions for
     * @return submissions for the given exam
     * @throws SQLException if unable to retrieve the submissions
     */
    public List<ExamSubmission> findAllForExam(UUID id) throws SQLException {
        String query = "SELECT * FROM examSubmissions WHERE examId = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<ExamSubmission> examSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ExamSubmission examSubmission = load(rs);

                IdentityMap.getInstance().put(
                    examSubmission.getId(),
                    examSubmission
                );

                examSubmissions.add(examSubmission);
            }

            return examSubmissions;
        }
    }

    /**
     * Retrieve all exam submissions for a given student ID.
     *
     * @param id ID of the student to retrieve submissions for
     * @return submissions for the given student
     * @throws SQLException if unable to retrieve the submissions
     */
    public List<ExamSubmission> findAllForStudent(UUID id) throws SQLException {
        String query = "SELECT examSubmissions.* FROM examSubmissions " +
            "WHERE examSubmissions.userId = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<ExamSubmission> examSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ExamSubmission examSubmission = load(rs);

                IdentityMap.getInstance().put(
                    examSubmission.getId(),
                    examSubmission
                );

                examSubmissions.add(examSubmission);
            }

            return examSubmissions;
        }
    }

    @Override
    public void insert(ExamSubmission examSubmission) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, marks) VALUES (?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
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

    @Override
    public void update(ExamSubmission examSubmission) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET marks = ? WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, examSubmission.getMarks());
            statement.setObject(2, examSubmission.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected ExamSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        int marks = rs.getInt("marks");
        return new ExamSubmission(id, marks);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
