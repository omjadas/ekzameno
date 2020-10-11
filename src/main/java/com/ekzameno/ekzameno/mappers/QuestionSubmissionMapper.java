package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for QuestionSubmissions.
 */
public class QuestionSubmissionMapper extends Mapper<QuestionSubmission> {
    private static final String tableName = "question_submissions";

    /**
     * Retrieve the QuestionSubmission with the given relation IDs.
     *
     * @param questionId       ID of the question
     * @param examSubmissionId ID of the exam submission
     * @return the QuestionSubmission with the specified relation IDs
     * @throws SQLException if unable to retrieve the QuestionSubmission
     */
    public QuestionSubmission findByRelationIds(
        UUID questionId,
        UUID examSubmissionId
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName +
            " WHERE question_id = ? AND exam_submission_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, questionId);
            statement.setObject(2, examSubmissionId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);
                IdentityMap.getInstance().put(
                    questionSubmission.getId(),
                    questionSubmission
                );
                return questionSubmission;
            } else {
                throw new NotFoundException();
            }
        }
    }

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
            " (id, answer, exam_submission_id, question_id, mark) VALUES (?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, questionSubmission.getId());
            statement.setString(2, questionSubmission.getAnswer());
            statement.setObject(3, questionSubmission.getExamSubmissionId());
            statement.setObject(4, questionSubmission.getQuestionId());
            statement.setObject(5, questionSubmission.getMark());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(QuestionSubmission questionSubmission)
            throws SQLException {
        String query = "UPDATE " + tableName +
            " SET answer = ?, exam_submission_id = ?, question_id = ?, mark = ? " +
            "WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, questionSubmission.getAnswer());
            statement.setObject(2, questionSubmission.getExamSubmissionId());
            statement.setObject(3, questionSubmission.getQuestionId());
            statement.setObject(4, questionSubmission.getId());
            statement.setObject(5, questionSubmission.getMark());
            statement.executeUpdate();
        }
    }

    @Override
    protected QuestionSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String answer = rs.getString("answer");
        UUID questionId = rs.getObject("question_id", java.util.UUID.class);
        UUID examSubmissionId = rs.getObject(
            "exam_submission_id",
            java.util.UUID.class
        );
        // int mark;
        // if(rs.wasNull())
        //     mark = rs.getInt("mark");
        // else
        int mark = rs.getInt("mark");
        return new QuestionSubmission(id, answer, questionId, examSubmissionId, mark);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
