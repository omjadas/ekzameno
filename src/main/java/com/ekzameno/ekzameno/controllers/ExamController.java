package com.ekzameno.ekzameno.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateExamDTO;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.services.ExamService;

/**
 * Controller for Exams.
 */
@Path("/exams")
public class ExamController {
    private ExamService examService = new ExamService();

    /**
     * update an exam.
     *
     * @param examId Id of th exam
     * @param dto Exam DTO
     * @return Response to the client
     */
    @Path("/exam")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateExam(
        CreateExamDTO dto
    ) {
        Exam exam = examService.updateExam(
            dto.name,
            dto.description,
            dto.startTime,
            dto.finishTime);
        if (exam != null) {
            return Response.ok().entity(exam).build();
        } else {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }
    }
}
