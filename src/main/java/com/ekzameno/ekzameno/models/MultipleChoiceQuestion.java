package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.AnswerMapper;

public class MultipleChoiceQuestion extends Question {
    public static final String TYPE = "MULTIPLE_CHOICE";
    private List<Answer> answers = null;

    public MultipleChoiceQuestion(UUID id, String question, int marks) {
        super(id, question, marks);
    }

    public MultipleChoiceQuestion(String question, int marks) {
        super(question, marks);
    }

    public List<Answer> getAnswers() throws SQLException {
        if (answers == null) {
            return new AnswerMapper().findAllForQuestion(getId());
        } else {
            return answers;
        }
    }
}
