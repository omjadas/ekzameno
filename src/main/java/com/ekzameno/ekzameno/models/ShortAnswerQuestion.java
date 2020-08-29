package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class ShortAnswerQuestion extends Question {
    public ShortAnswerQuestion(UUID id, String question, int marks) {
        super(question, marks);
        this.setId(id);
    }

    public ShortAnswerQuestion(String question, int marks) {
        super(question, marks);
    }
}
