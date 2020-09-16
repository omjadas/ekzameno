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

import com.ekzameno.ekzameno.dtos.CreateExamDTO;
import com.ekzameno.ekzameno.filters.Protected;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.services.ExamService;

/**
 * Controller for Exams.
 */
@Path("/exams")
@Protected
public class ExamController {
    private ExamService examService = new ExamService();

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
    public Response updateExam(
        @PathParam("examId") String examId,
        CreateExamDTO dto
    ) {
        Exam exam = examService.updateExam(
            dto.name,
            dto.description,
            dto.startTime,
            dto.finishTime,
            UUID.fromString(examId)
        );
        if (exam != null) {
            return Response.ok().entity(exam).build();
        } else {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    /**
     * Delete an exam.
     *
     * @param examId Id of th exam
     * @return Response to the client
     */
    @Path("/{examId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteExam(
        @PathParam("examId") String examId
    ) {
        examService.deleteExam(UUID.fromString(examId));
        return Response
            .status(Response.Status.NO_CONTENT)
            .build();
    }
}
