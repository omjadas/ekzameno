package com.ekzameno.ekzameno.controllers;

import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateOptionDTO;
import com.ekzameno.ekzameno.dtos.CreateQuestionDTO;
import com.ekzameno.ekzameno.dtos.CreateQuestionSubmissionDTO;
import com.ekzameno.ekzameno.dtos.UpdateQuestionSubmissionDTO;
import com.ekzameno.ekzameno.filters.Protected;
import com.ekzameno.ekzameno.models.Option;
import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.services.OptionService;
import com.ekzameno.ekzameno.services.QuestionService;

/**
 * Controller for questions.
 */
@Path("/questions")
@Protected
public class QuestionController {
    private QuestionService questionService = new QuestionService();
    private OptionService optionService = new OptionService();

    /**
     * Update a question.
     *
     * @param questionId ID of the exam
     * @param headers    Request headers
     * @param dto        Question DTO
     * @return Response to the client
     */
    @Path("/{questionId}")
    @PUT
    @RolesAllowed({ "instructor" })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Question updateQuestion(
        @PathParam("questionId") String questionId,
        @Context HttpHeaders headers,
        CreateQuestionDTO dto
    ) {
        return questionService.updateQuestion(
            dto.question,
            dto.marks,
            UUID.fromString(questionId),
            headers.getHeaderString("if-match")
        );
    }

    /**
     * Delete a question.
     *
     * @param questionId Id of the question
     * @return Response to the client
     */
    @Path("/{questionId}")
    @DELETE
    @RolesAllowed({ "instructor" })
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
    @RolesAllowed({ "instructor", "student" })
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
    @RolesAllowed({ "instructor" })
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

    /**
     * Create a question submission.
     *
     * @param questionId       ID of the question the submission belongs to
     * @param examSubmissionId ID of the exam submission the submission belongs
     *                         to
     * @param dto              question submission DTO
     * @return created question submission
     */
    @Path("/{questionId}/submissions/{examSubmissionId}")
    @POST
    @RolesAllowed({ "instructor", "student" })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public QuestionSubmission createSubmission(
        @PathParam("questionId") String questionId,
        @PathParam("examSubmissionId") String examSubmissionId,
        CreateQuestionSubmissionDTO dto
    ) {
        return questionService.createSubmission(
            UUID.fromString(questionId),
            UUID.fromString(examSubmissionId),
            dto.answer,
            dto.marks
        );
    }

    /**
     * Update a question submission.
     *
     * @param questionId       ID of the question the submission belongs to
     * @param examSubmissionId ID of the exam submission the submission belongs
     *                         to
     * @param headers          headers for the request
     * @param dto              question submission DTO
     * @return updated question submission
     */
    @Path("/{questionId}/submissions/{examSubmissionId}")
    @PUT
    @RolesAllowed({ "instructor" })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public QuestionSubmission updateSubmission(
        @PathParam("questionId") String questionId,
        @PathParam("examSubmissionId") String examSubmissionId,
        @Context HttpHeaders headers,
        UpdateQuestionSubmissionDTO dto
    ) {
        return questionService.updateSubmission(
            UUID.fromString(questionId),
            UUID.fromString(examSubmissionId),
            dto.marks,
            headers.getHeaderString("if-match")
        );
    }
}
