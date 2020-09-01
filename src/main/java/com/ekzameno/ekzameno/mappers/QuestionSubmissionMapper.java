package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for QuestionSubmissions.
 */
public class QuestionSubmissionMapper extends Mapper<QuestionSubmission> {
    private static final String tableName = "question_submissions";

    /**
     * Retrieve all questions submissions for a given exam submission ID.
     *
     * @param id ID of the exam submission to retrieve the question submissions
     *           for
     * @return questions submissions for the given exam submission
     * @throws SQLException if unable to retrieve the question submissions
     */
    public List<QuestionSubmission> findAllForExamSubmission(UUID id)
            throws SQLException {
        String query = "SELECT * FROM " + tableName +
            " WHERE exam_submission_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<QuestionSubmission> questionSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);

                IdentityMap.getInstance().put(
                    questionSubmission.getId(),
                    questionSubmission
                );

                questionSubmissions.add(load(rs));
            }

            return questionSubmissions;
        }
    }

    /**
     * Retrieve all question submissions for a given question ID.
     *
     * @param id ID of the question to retrieve submissions for
     * @return submissions for the given question
     * @throws SQLException if unable to retrieve the question submissions
     */
    public List<QuestionSubmission> findAllForQuestion(UUID id)
            throws SQLException {
        String query = "SELECT question_submissions.* " +
            "FROM question_submissions " +
            "WHERE question_submissions.question_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<QuestionSubmission> questionSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);

                IdentityMap.getInstance().put(
                    questionSubmission.getId(),
                    questionSubmission
                );

                questionSubmissions.add(questionSubmission);
            }

            return questionSubmissions;
        }
    }

    @Override
    public void insert(QuestionSubmission questionSubmission)
            throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answer) VALUES (?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, questionSubmission.getId());
            statement.setString(2, questionSubmission.getAnswer());
            statement.executeUpdate();
            IdentityMap.getInstance().put(
                questionSubmission.getId(),
                questionSubmission
            );
        }
    }

    @Override
    public void update(QuestionSubmission questionSubmission)
            throws SQLException {
        String query = "UPDATE " + tableName + " SET answer = ? WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, questionSubmission.getAnswer());
            statement.setObject(2, questionSubmission.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected QuestionSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String answer = rs.getString("answer");
        return new QuestionSubmission(id, answer);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
