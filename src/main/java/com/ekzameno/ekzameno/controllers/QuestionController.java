package com.ekzameno.ekzameno.controllers;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateOptionDTO;
import com.ekzameno.ekzameno.dtos.CreateQuestionDTO;
import com.ekzameno.ekzameno.models.Option;
import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.services.OptionService;
import com.ekzameno.ekzameno.services.QuestionService;

/**
 * Controller for questions.
 */
@Path("/questions")
public class QuestionController {
    private QuestionService questionService = new QuestionService();
    private OptionService optionService = new OptionService();

    /**
     * Update a question.
     *
     * @param questionId Id of th exam
     * @param dto        Question DTO
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

    /**
     * Fetch options for a given question.
     *
     * @param questionId ID of the question to fetch options for
     * @return options associated with the given question
     */
    @Path("/{questionId}/options")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Option> getOptions(@PathParam("questionId") String questionId) {
        return optionService.getOptionsForQuestion(UUID.fromString(questionId));
    }

    /**
     * Create an option for a given question.
     *
     * @param questionId ID of the question to create the option for
     * @param dto        option DTO
     * @return created option
     */
    @Path("/{questionId}/options")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Option createOption(
        @PathParam("questionId") String questionId,
        CreateOptionDTO dto
    ) {
        return optionService.createOption(
            dto.answer,
            dto.correct,
            UUID.fromString(questionId)
        );
    }
}
