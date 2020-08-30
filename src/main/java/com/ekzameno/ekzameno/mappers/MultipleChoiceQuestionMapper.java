package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.MultipleChoiceQuestion;
import com.ekzameno.ekzameno.shared.IdentityMap;

public class MultipleChoiceQuestionMapper
        extends AbstractQuestionMapper<MultipleChoiceQuestion> {
    protected MultipleChoiceQuestion load(ResultSet rs) throws SQLException {
        Exam exam = new ExamMapper().load(rs);
        UUID id = rs.getObject("questions_id", java.util.UUID.class);
        String question = rs.getString("questions_question");
        int marks = rs.getInt("questions_marks");
        MultipleChoiceQuestion multipleChoiceQuestion =
            new MultipleChoiceQuestion(id, question, marks, exam);
        IdentityMap.getInstance().put(id, multipleChoiceQuestion);
        return multipleChoiceQuestion;
    }

    protected String getType() {
        return MultipleChoiceQuestion.TYPE;
    }
}
