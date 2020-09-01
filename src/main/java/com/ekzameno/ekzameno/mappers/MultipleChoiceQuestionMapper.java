package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.MultipleChoiceQuestion;

/**
 * Data Mapper for MultipleChoiceQuestions.
 */
public class MultipleChoiceQuestionMapper
        extends AbstractQuestionMapper<MultipleChoiceQuestion> {
    @Override
    protected MultipleChoiceQuestion load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        String question = rs.getString("question");
        int marks = rs.getInt("marks");
        UUID examId = rs.getObject("exam_id", java.util.UUID.class);
        return new MultipleChoiceQuestion(id, question, marks, examId);
    }

    @Override
    protected String getType() {
        return MultipleChoiceQuestion.TYPE;
    }
}
