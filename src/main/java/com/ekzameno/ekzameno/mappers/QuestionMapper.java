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

public class QuestionMapper extends AbstractQuestionMapper<Question> {
    public List<Question> findAllForExam(UUID id) throws SQLException {
        String query = "SELECT * FROM questions WHERE examId = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            List<Question> questions = new ArrayList<>();

            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                questions.add(load(rs));
            }

            return questions;
        }
    }

    public void insert(Question obj) throws SQLException {
        Mapper<Question> mapper = Mapper.getMapper(obj.getClass());
        mapper.insert(obj);
    }

    protected Question load(ResultSet rs) throws SQLException {
        String type = rs.getString("type");

        Mapper<?> mapper;

        if (type == MultipleChoiceQuestion.TYPE) {
            mapper = new MultipleChoiceQuestionMapper();
        } else if (type == ShortAnswerQuestion.TYPE) {
            mapper = new ShortAnswerQuestionMapper();
        } else {
            throw new RuntimeException();
        }

        return (Question) mapper.load(rs);
    }

    protected String getType() {
        return "";
    }
}
