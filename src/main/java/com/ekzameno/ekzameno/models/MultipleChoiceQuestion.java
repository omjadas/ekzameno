package com.ekzameno.ekzameno.models;

import java.util.List;
import java.util.UUID;

public class MultipleChoiceQuestion extends Question {
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
