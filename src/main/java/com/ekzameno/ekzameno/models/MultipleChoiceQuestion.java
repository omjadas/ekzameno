package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.AnswerMapper;

/**
 * MultipleChoiceQuestion for an Exam.
 */
public class MultipleChoiceQuestion extends Question {
    public static final String TYPE = "MULTIPLE_CHOICE";
    private List<Answer> answers = null;

    /**
     * Create a MultipleChoiceQuestion with an ID.
     *
     * @param id       ID of the MultipleChoiceQuestion
     * @param question question of the MultipleChoiceQuestion
     * @param marks    number of marks allocated to the MultipleChoiceQuestion
     */
    public MultipleChoiceQuestion(UUID id, String question, int marks) {
        super(id, question, marks);
    }

    /**
     * Create a MultipleChoiceQuestion without an ID (registers as new).
     *
     * @param question question of the MultipleChoiceQuestion
     * @param marks    number of marks allocated to the MultipleChoiceQuestion
     */
    public MultipleChoiceQuestion(String question, int marks) {
        super(question, marks);
    }

    /**
     * Retrieve the possible answers for the MultipleChoiceQuestion.
     *
     * @return possible answers for the MultipleChoiceQuestion
     * @throws SQLException if unable to retrieve the answers
     */
    public List<Answer> getAnswers() throws SQLException {
        if (answers == null) {
            return new AnswerMapper().findAllForQuestion(getId());
        } else {
            return answers;
        }
    }
}
