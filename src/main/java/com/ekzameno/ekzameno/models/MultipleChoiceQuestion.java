package com.ekzameno.ekzameno.models;

import java.util.List;
import java.util.UUID;

public class MultipleChoiceQuestion extends Question {
    public static final String TYPE = "MULTIPLE_CHOICE";

    public MultipleChoiceQuestion(UUID id, String question, int marks) {
        super(question, marks);
        setId(id);
    }

    public MultipleChoiceQuestion(String question, int marks) {
        super(question, marks);
    }

    public List<Answer> getAnswers() {
        return null;
    }
}
