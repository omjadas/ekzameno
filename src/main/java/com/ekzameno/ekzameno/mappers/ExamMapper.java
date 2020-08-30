package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class ExamMapper extends Mapper<Exam> {
    private static final String tableName = "exams " +
        "JOIN subjects on exams.subjectId = subjects.id";
    private static final String columns = "exams.id AS exams_id, " +
        "exams.name AS exams_name, " +
        "exams.publishDate AS exams_publishDate, " +
        "exams.closeDate AS exams_closeDate";

    public void insert(Exam exam) throws SQLException {
        String query = "INSERT INTO " + tableName +
            " (id, name, publishDate, closeDate, subjectId) " +
            "VALUES (?,?,?,?,?)";

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
            statement.setObject(5, exam.getSubject().getId());
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
        Subject subject = new SubjectMapper().load(rs);
        UUID examId = rs.getObject("exams_id", java.util.UUID.class);
        String examName = rs.getString("exams_name");
        Date publishDate = rs.getTimestamp("exams_publishDate");
        Date closeDate = rs.getTimestamp("exams_closeDate");
        return new Exam(examId, examName, publishDate, closeDate, subject);
    }

    protected String getTableName() {
        return tableName;
    }

    protected String getColumns() {
        return columns;
    }
}
