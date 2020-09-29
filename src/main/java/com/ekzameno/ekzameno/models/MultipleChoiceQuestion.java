package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.proxies.OptionProxyList;
import com.ekzameno.ekzameno.proxies.ProxyList;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * MultipleChoiceQuestion for an Exam.
 */
public class MultipleChoiceQuestion extends Question {
    public static final String TYPE = "MULTIPLE_CHOICE";
    @JsonIgnore
    private ProxyList<Option> options;

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
        super(id, question, marks, examId, TYPE);
        this.options = new OptionProxyList(id);
    }

    /**
     * Create a MultipleChoiceQuestion without an ID (registers as new).
     *
     * @param question question of the MultipleChoiceQuestion
     * @param marks    number of marks allocated to the MultipleChoiceQuestion
     * @param examId   ID of the related exam
     */
    public MultipleChoiceQuestion(String question, int marks, UUID examId) {
        super(question, marks, examId, TYPE);
        this.options = new OptionProxyList(getId());
    }

    /**
     * Retrieve the possible options for the MultipleChoiceQuestion.
     *
     * @return possible options for the MultipleChoiceQuestion
     */
    public ProxyList<Option> getOptions() {
        return options;
    }
}
