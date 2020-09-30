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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.ekzameno.ekzameno.dtos.CreateExamDTO;
import com.ekzameno.ekzameno.dtos.CreateExamSubmissionDTO;
import com.ekzameno.ekzameno.dtos.CreateQuestionDTO;
import com.ekzameno.ekzameno.filters.Protected;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.ExamSubmission;
import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.services.ExamService;
import com.ekzameno.ekzameno.services.QuestionService;

/**
 * Controller for Exams.
 */
@Path("/exams")
@Protected
public class ExamController {
    private ExamService examService = new ExamService();
    private QuestionService questionService = new QuestionService();

    /**
     * Fetches an exam for a given slug.
     *
     * @param slug exam's slug
     * @return exam
     */
    @Path("/{slug}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Exam getExam(@PathParam("slug") String slug) {
        return examService.getExam(slug);
    }

    /**
     * Update an exam.
     *
     * @param examId Id of th exam
     * @param dto Exam DTO
     * @return Response to the client
     */
    @Path("/{examId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Exam updateExam(
        @PathParam("examId") String examId,
        CreateExamDTO dto
    ) {
        return examService.updateExam(
            dto.name,
            dto.description,
            dto.startTime,
            dto.finishTime,
            UUID.fromString(examId)
        );
    }

    /**
     * Delete an exam.
     *
     * @param examId Id of the exam
     * @return Response to the client
     */
    @Path("/{examId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteExam(@PathParam("examId") String examId) {
        examService.deleteExam(UUID.fromString(examId));
        return Response
            .status(Response.Status.NO_CONTENT)
            .build();
    }

    /**
     * Create a question for a given exam.
     *
     * @param examId ID of the exam to create the question for
     * @param dto Question DTO
     * @return Response to the client
     */
    @Path("/{examId}/questions")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Question createQuestion(
        @PathParam("examId") String examId,
        CreateQuestionDTO dto
    ) {
        return questionService.createQuestion(
            UUID.fromString(examId),
            dto.question,
            dto.marks,
            dto.type,
            dto.options
        );
    }

    /**
     * Retrieve questions for a given exam.
     *
     * @param examId ID of the exam to retrieve questions for
     * @return questions for the specified exam
     */
    @Path("/{examId}/questions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Question> getQuestions(@PathParam("examId") String examId) {
        return questionService.getQuestionsForExam(UUID.fromString(examId));
    }

    /**
     * Create a submission for a given exam.
     *
     * @param examId          ID of the exam to create the question for
     * @param dto             Question DTO
     * @param securityContext Security context for the request
     * @return Response to the client
     */
    @Path("/{examId}/submission")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ExamSubmission submitExam(
        @PathParam("examId") String examId,
        @Context SecurityContext securityContext,
        CreateExamSubmissionDTO dto
    ) {
        return examService.createSubmission(
            UUID.fromString(examId),
            UUID.fromString(securityContext.getUserPrincipal().getName())
        );
    }
}
