package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.ShortAnswerQuestion;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Data Mapper for ShortAnswerQuestions.
 */
public class ShortAnswerQuestionMapper
        extends AbstractQuestionMapper<ShortAnswerQuestion> {
    @Override
    protected ShortAnswerQuestion load(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", java.util.UUID.class);
        ShortAnswerQuestion shortAnswerQuestion =
            (ShortAnswerQuestion) IdentityMap
                .getCurrent()
                .get(id);
        if (shortAnswerQuestion != null) {
            return shortAnswerQuestion;
        }
        String question = rs.getString("question");
        int marks = rs.getInt("marks");
        UUID examId = rs.getObject("exam_id", java.util.UUID.class);
        return new ShortAnswerQuestion(id, question, marks, examId);
    }

    @Override
    protected String getType() {
        return ShortAnswerQuestion.TYPE;
    }
}
