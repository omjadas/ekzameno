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
     * @param forUpdate        whether the row should be locked
     * @return the QuestionSubmission with the specified relation IDs
     * @throws SQLException if unable to retrieve the QuestionSubmission
     */
    public QuestionSubmission findByRelationIds(
        UUID questionId,
        UUID examSubmissionId,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName +
            " WHERE question_id = ? AND exam_submission_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, questionId);
            statement.setObject(2, examSubmissionId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);
                IdentityMap.getCurrent().put(
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
        return findByRelationIds(questionId, examSubmissionId, false);
    }

    /**
     * Retrieve all questions submissions for a given exam submission ID.
     *
     * @param id        ID of the exam submission to retrieve the question
     *                  submissions for
     * @param forUpdate whether the rows should be locked
     * @return questions submissions for the given exam submission
     * @throws SQLException if unable to retrieve the question submissions
     */
    public List<QuestionSubmission> findAllForExamSubmission(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName +
            " WHERE exam_submission_id = ?" + (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<QuestionSubmission> questionSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);

                IdentityMap.getCurrent().put(
                    questionSubmission.getId(),
                    questionSubmission
                );

                questionSubmissions.add(load(rs));
            }

            return questionSubmissions;
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
        return findAllForExamSubmission(id, false);
    }

    /**
     * Retrieve all question submissions for a given question ID.
     *
     * @param id        ID of the question to retrieve submissions for
     * @param forUpdate whether the rows should be locked
     * @return submissions for the given question
     * @throws SQLException if unable to retrieve the question submissions
     */
    public List<QuestionSubmission> findAllForQuestion(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT question_submissions.* " +
            "FROM question_submissions " +
            "WHERE question_submissions.question_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<QuestionSubmission> questionSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                QuestionSubmission questionSubmission = load(rs);

                IdentityMap.getCurrent().put(
                    questionSubmission.getId(),
                    questionSubmission
                );

                questionSubmissions.add(questionSubmission);
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
        return findAllForQuestion(id, false);
    }

    @Override
    public void insert(QuestionSubmission questionSubmission)
            throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, answer, exam_submission_id, question_id, marks) " +
            "VALUES (?,?,?,?,?)";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, questionSubmission.getId());
            statement.setString(2, questionSubmission.getAnswer());
            statement.setObject(3, questionSubmission.getExamSubmissionId());
            statement.setObject(4, questionSubmission.getMarks());
            statement.setObject(5, questionSubmission.getQuestionId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(QuestionSubmission questionSubmission)
            throws SQLException {
        String query = "UPDATE " + tableName +
            " SET answer = ?, exam_submission_id = ?, question_id = ?, " +
            "marks = ? WHERE id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, questionSubmission.getAnswer());
            statement.setObject(2, questionSubmission.getExamSubmissionId());
            statement.setObject(3, questionSubmission.getQuestionId());
            statement.setObject(4, questionSubmission.getMarks());
            statement.setObject(5, questionSubmission.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected QuestionSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        QuestionSubmission questionSubmission =
            (QuestionSubmission) IdentityMap
                .getCurrent()
                .get(id);
        if (questionSubmission != null) {
            return questionSubmission;
        }
        String answer = rs.getString("answer");
        UUID questionId = rs.getObject("question_id", java.util.UUID.class);
        UUID examSubmissionId = rs.getObject(
            "exam_submission_id",
            java.util.UUID.class
        );
        Integer marks = rs.getInt("marks");
        marks = rs.wasNull() ? null : marks;
        return new QuestionSubmission(id, answer, questionId,
        examSubmissionId, marks);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
