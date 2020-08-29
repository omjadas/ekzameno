package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ekzameno.ekzameno.models.MultipleChoiceQuestion;
import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.models.ShortAnswerQuestion;

public class QuestionMapper extends AbstractQuestionMapper<Question> {
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
