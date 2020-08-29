package com.ekzameno.ekzameno.models;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question {
    public MultipleChoiceQuestion(String question, int marks) {
        super(question, marks);
    }

    public List<Answer> getAnswers() {
        return null;
    }
}
