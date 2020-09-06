package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Answer for a MultipleChoiceQuestion.
 */
public class Answer extends Model {
    private String answer;
    private boolean correct;
    private UUID questionId;
    private Question question = null;

    /**
     * Create an Answer with an ID.
     *
     * @param id         ID of the Answer
     * @param answer     answer of the Answer
     * @param correct    whether the Answer is the correct answer for the
     *                   question
     * @param questionId ID of the related question
     */
    public Answer(UUID id, String answer, boolean correct, UUID questionId) {
        super(id);
        this.answer = answer;
        this.correct = correct;
        this.questionId = questionId;
    }

    /**
     * Create an Answer without an ID (registers as new).
     *
     * @param answer     answer of the Answer
     * @param correct    whether the Answer is the correct answer for the
     *                   question
     * @param questionId ID of the related question
     */
    public Answer(String answer, boolean correct, UUID questionId) {
        this.answer = answer;
        this.correct = correct;
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    /**
     * Set the answer for the answer (registers the Answer as dirty).
     *
     * @param answer answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set whether the Answer is correct (registers the Answer as dirty).
     *
     * @param correct whether the Answer is correct
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Retrieve the related question.
     *
     * @return related question
     * @throws SQLException if unable to retrieve the question
     */
    public Question getQuestion() throws SQLException {
        if (question == null) {
            question = new QuestionMapper().findById(questionId);
        }
        return question;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    /**
     * Set the related question (marks the Answer as dirty).
     *
     * @param question relation question
     */
    public void setQuestion(Question question) {
        this.question = question;
        this.questionId = question.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the ID of the related question (marks the Answer as dirty).
     *
     * @param questionId ID of the related question
     */
    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
        this.question = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
