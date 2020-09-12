package com.ekzameno.ekzameno.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateSubjectDTO;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.services.SubjectService;

/**
 * Servlet implementation class SubjectServlet.
 */
@Path("/subjects")
public class SubjectController {
    private SubjectService subjectService = new SubjectService();

    /**
     * Handles creation of new subject.
     *
     * @param dto Subject DTO
     * @return Response to the client
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSubject(CreateSubjectDTO dto) {
        Subject subject = subjectService.createSubject(
            dto.name,
            dto.description,
            dto.instructors,
            dto.students);

        if (subject != null) {
            return Response.ok().entity(subject).build();
        } else {
            return Response.status(Response
            .Status
            .INTERNAL_SERVER_ERROR)
            .build();
        }
    }
}
