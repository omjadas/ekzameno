package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.proxies.AnswerProxyList;
import com.ekzameno.ekzameno.proxies.ProxyList;

/**
 * MultipleChoiceQuestion for an Exam.
 */
public class MultipleChoiceQuestion extends Question {
    public static final String TYPE = "MULTIPLE_CHOICE";
    private ProxyList<Answer> answers;

    /**
     * Create a MultipleChoiceQuestion with an ID.
     *
     * @param id       ID of the MultipleChoiceQuestion
     * @param question question of the MultipleChoiceQuestion
     * @param marks    number of marks allocated to the MultipleChoiceQuestion
     * @param examId   ID of the related exam
     */
    public MultipleChoiceQuestion(
        UUID id,
        String question,
        int marks,
        UUID examId
    ) {
        super(id, question, marks, examId);
        this.answers = new AnswerProxyList(id);
    }

    /**
     * Create a MultipleChoiceQuestion without an ID (registers as new).
     *
     * @param question question of the MultipleChoiceQuestion
     * @param marks    number of marks allocated to the MultipleChoiceQuestion
     * @param examId   ID of the related exam
     */
    public MultipleChoiceQuestion(String question, int marks, UUID examId) {
        super(question, marks, examId);
        this.answers = new AnswerProxyList(getId());
    }

    /**
     * Retrieve the possible answers for the MultipleChoiceQuestion.
     *
     * @return possible answers for the MultipleChoiceQuestion
     */
    public ProxyList<Answer> getAnswers() {
        return answers;
    }
}
