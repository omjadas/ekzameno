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
import com.ekzameno.ekzameno.services.CreateExamService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Controller for Exam Page
 * @return response for creating the exam
 */
public class ExamController {
    private CreateExamService examCreation = new CreateExamService();
    @Path("/createExam")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExam(Exam exam) {
        examCreation.CreateAnExam(exam);// CreateanExam
        return Response.ok().build();
    }
}
