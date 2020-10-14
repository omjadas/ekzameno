package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.MultipleChoiceQuestion;
import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.models.ShortAnswerQuestion;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for Questions.
 */
public class QuestionMapper extends AbstractQuestionMapper<Question> {
    /**
     * Retrieve all questions for a given exam ID.
     *
     * @param id        ID of the exam to retrieve questions for
     * @param forUpdate whether the rows should be updated
     * @return questions for the given exam
     * @throws SQLException if unable to retrieve the questions
     */
    public List<Question> findAllForExam(
        UUID id,
        boolean forUpdate
    ) throws SQLException {
        String query = "SELECT * FROM questions WHERE exam_id = ?" +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Question> questions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Question question = load(rs);
                IdentityMap.getCurrent().put(question.getId(), question);
                questions.add(question);
            }

            return questions;
        }
    }

    /**
     * Retrieve all questions for a given exam ID.
     *
     * @param id ID of the exam to retrieve questions for
     * @return questions for the given exam
     * @throws SQLException if unable to retrieve the questions
     */
    public List<Question> findAllForExam(UUID id) throws SQLException {
        return findAllForExam(id, false);
    }

    @Override
    protected Question load(ResultSet rs) throws SQLException {
        String type = rs.getString("type");

        Mapper<?> mapper;

        if (type.equals(MultipleChoiceQuestion.TYPE)) {
            mapper = new MultipleChoiceQuestionMapper();
        } else if (type.equals(ShortAnswerQuestion.TYPE)) {
            mapper = new ShortAnswerQuestionMapper();
        } else {
            throw new RuntimeException();
        }

        return (Question) mapper.load(rs);
    }

    @Override
    protected String getType() {
        return "";
    }
}
