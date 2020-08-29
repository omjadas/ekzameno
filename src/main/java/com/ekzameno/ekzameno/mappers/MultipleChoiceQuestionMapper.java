package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.MultipleChoiceQuestion;

public class MultipleChoiceQuestionMapper extends AbstractQuestionMapper<MultipleChoiceQuestion> {
    protected MultipleChoiceQuestion load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String question = rs.getString("question");
        int marks = rs.getInt("marks");
        return new MultipleChoiceQuestion(id, question, marks);
    }

    protected String getType() {
        return MultipleChoiceQuestion.TYPE;
    }
}
