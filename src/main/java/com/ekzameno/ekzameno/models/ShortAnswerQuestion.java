package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class ShortAnswerQuestion extends Question {
    public static final String TYPE = "SHORT_ANSWER";

    public ShortAnswerQuestion(UUID id, String question, int marks, Exam exam) {
        super(id, question, marks, exam);
    }

    public ShortAnswerQuestion(String question, int marks, Exam exam) {
        super(question, marks, exam);
    }
}
