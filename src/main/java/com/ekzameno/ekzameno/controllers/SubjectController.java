package com.ekzameno.ekzameno.controllers;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateExamDTO;
import com.ekzameno.ekzameno.dtos.CreateSubjectDTO;
import com.ekzameno.ekzameno.filters.Protected;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.services.ExamService;
import com.ekzameno.ekzameno.services.SubjectService;

/**
 * Controller for subjects.
 */
@Path("/subjects")
@Protected
public class SubjectController {
    private final SubjectService subjectService = new SubjectService();
    private final ExamService examService = new ExamService();

    /**
     * Handles the fetching of all subjects from the database.
     *
     * @return list of all subjects in the database.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Subject> getSubjects() {
        return subjectService.getSubjects();
    }

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

    /**
     * Handles the fetching of all exams from the database.
     *
     * @param subjectId ID of the subject to create the exam for
     * @return list of all exams for the subject
     */
    @Path("/{subjectId}/exams")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exam> getExamsForSubject(
        @PathParam("subjectId") String subjectId) {
        return examService.getExamsForSubject(UUID.fromString(subjectId));
    }

    /**
     * Create an exam.
     *
     * @param subjectId ID of the subject to create the exam for
     * @param dto Exam DTO
     * @return Response to the client
     */
    @Path("/{subjectId}/exams")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExam(
        @PathParam("subjectId") String subjectId,
        CreateExamDTO dto
    ) {
        Exam exam = examService.createExam(
            dto.name,
            dto.description,
            dto.startTime,
            dto.finishTime,
            UUID.fromString(subjectId));
        if (exam != null) {
            return Response.ok().entity(exam).build();
        } else {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }
    }
}
