package com.ekzameno.ekzameno.controllers;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.services.QuestionService;

/**
 * Controller for questions.
 */
@Path("/questions")
public class QuestionController {
    private QuestionService questionService = new QuestionService();

    /**
     * Retrieve questions for a given exam.
     *
     * @param examId ID of the exam to retrieve questions for
     * @return questions for the specified exam
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Question> getQuestions(
        @PathParam("examId") String examId
    ) {
        return questionService.getQuestionsForExam(UUID.fromString(examId));
    }
}
