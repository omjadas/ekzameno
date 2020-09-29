package com.ekzameno.ekzameno.controllers;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateQuestionDTO;
import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.services.QuestionService;

/**
 * Controller for questions.
 */
@Path("/questions")
public class QuestionController {
    private QuestionService questionService = new QuestionService();

    /**
     * Update a question.
     *
     * @param questionId Id of th exam
     * @param dto Question DTO
     * @return Response to the client
     */
    @Path("/{questionId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateQuestion(
        @PathParam("questionId") String questionId,
        CreateQuestionDTO dto
    ) {
        Question question = questionService.updateQuestion(
            dto.question,
            dto.marks,
            dto.type,
            dto.options,
            UUID.fromString(questionId)
        );
        if (question != null) {
            return Response.ok().entity(question).build();
        } else {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    /**
     * Delete a question.
     *
     * @param questionId Id of the question
     * @return Response to the client
     */
    @Path("/{questionId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteExam(
        @PathParam("questionId") String questionId
    ) {
        questionService.deleteQuestion(UUID.fromString(questionId));
        return Response
            .status(Response.Status.NO_CONTENT)
            .build();
    }
}
