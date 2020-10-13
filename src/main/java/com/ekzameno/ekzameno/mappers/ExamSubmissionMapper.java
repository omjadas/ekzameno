package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.models.ExamSubmission;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for ExamSubmissions.
 */
public class ExamSubmissionMapper extends Mapper<ExamSubmission> {
    private static final String tableName = "exam_submissions";

    /**
     * Find the ExamSubmission with the given relation IDs.
     *
     * @param studentId ID of the student
     * @param examId    ID of the exam
     * @return the ExamSubmission with the given relation IDs
     * @throws SQLException if unable to retrieve the ExamSubmission
     */
    public ExamSubmission findByRelationIds(
        UUID studentId,
        UUID examId
    ) throws SQLException {
        String query = "SELECT * FROM " + tableName +
            " WHERE user_id = ? AND exam_id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, studentId);
            statement.setObject(2, examId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ExamSubmission examSubmission = load(rs);
                IdentityMap.getCurrent().put(
                    examSubmission.getId(),
                    examSubmission
                );
                return examSubmission;
            } else {
                throw new NotFoundException();
            }
        }
    }

    /**
     * Retrieve all exam submissions for a given exam ID.
     *
     * @param id ID of the exam to retrieve submissions for
     * @return submissions for the given exam
     * @throws SQLException if unable to retrieve the submissions
     */
    public List<ExamSubmission> findAllForExam(UUID id) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE exam_id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<ExamSubmission> examSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ExamSubmission examSubmission = load(rs);

                IdentityMap.getCurrent().put(
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
        String query = "SELECT * FROM " + tableName +
            " WHERE exam_submissions.user_id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<ExamSubmission> examSubmissions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ExamSubmission examSubmission = load(rs);

                IdentityMap.getCurrent().put(
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
            " (id, marks, user_id, exam_id) VALUES (?,?,?,?)";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, examSubmission.getId());
            statement.setInt(2, examSubmission.getMarks());
            statement.setObject(3, examSubmission.getStudentId());
            statement.setObject(4, examSubmission.getExamId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(ExamSubmission examSubmission) throws SQLException {
        String query = "UPDATE " + tableName +
            " SET marks = ?, user_id = ?, exam_id = ? WHERE id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, examSubmission.getMarks());
            statement.setObject(2, examSubmission.getStudentId());
            statement.setObject(3, examSubmission.getExamId());
            statement.setObject(4, examSubmission.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected ExamSubmission load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        ExamSubmission examSubmission = (ExamSubmission) IdentityMap
           .getCurrent()
           .get(id);
        if (examSubmission != null) {
            return examSubmission;
        }
        int marks = rs.getInt("marks");
        UUID studentId = rs.getObject("user_id", java.util.UUID.class);
        UUID examId = rs.getObject("exam_id", java.util.UUID.class);
        return new ExamSubmission(id, marks, studentId, examId);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }
}
