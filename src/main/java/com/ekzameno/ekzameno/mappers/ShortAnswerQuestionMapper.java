package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.ShortAnswerQuestion;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class ShortAnswerQuestionMapper
        extends AbstractQuestionMapper<ShortAnswerQuestion> {
    protected ShortAnswerQuestion load(ResultSet rs) throws SQLException {
        Exam exam = new ExamMapper().load(rs);

        UUID id = rs.getObject("questions_id", java.util.UUID.class);
        String question = rs.getString("questions_question");
        int marks = rs.getInt("questions_marks");

        ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion(
            id,
            question,
            marks,
            exam
        );

        IdentityMap.getInstance().put(id, shortAnswerQuestion);

        return shortAnswerQuestion;
    }

    protected String getType() {
        return ShortAnswerQuestion.TYPE;
    }
}
