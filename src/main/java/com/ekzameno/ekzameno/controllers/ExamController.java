package com.ekzameno.ekzameno.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.filters.Protected;
import com.ekzameno.ekzameno.models.Exam;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Controller for Exam Page.
 * @return response for creating the exam
 */
@Path("/exams")
public class ExamController {
    private ExamService examService = new ExamService();
    /**
     * Handles creation of new exam.
     *
     * @param dto exam DTO
     * @return response to the client
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExam(CreateExamDto dto) {
        Exam exam = examService.createExam(
        dto.name,
        dto.description,
        dto.publishDate,
        dto.closeDate);
        
        if(exam != null) {
            return Response.ok().entity(exam).build();
        } else {
            return Response.status(Response
            .Status
            .INTERNAL_SERVER_ERROR)
            .build();
        }
    }
}
