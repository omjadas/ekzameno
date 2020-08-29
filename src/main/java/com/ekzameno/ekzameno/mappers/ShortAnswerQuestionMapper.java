package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.ShortAnswerQuestion;

public class ShortAnswerQuestionMapper
        extends AbstractQuestionMapper<ShortAnswerQuestion> {
    protected ShortAnswerQuestion load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String question = rs.getString("question");
        int marks = rs.getInt("marks");
        return new ShortAnswerQuestion(id, question, marks);
    }

    protected String getType() {
        return ShortAnswerQuestion.TYPE;
    }
}
