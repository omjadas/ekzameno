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
import com.ekzameno.ekzameno.dtos.CreateSubjectDTO;
import com.ekzameno.ekzameno.filters.Protected;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.Instructor;
import com.ekzameno.ekzameno.models.Student;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.services.ExamService;
import com.ekzameno.ekzameno.services.SubjectService;
import com.ekzameno.ekzameno.services.UserService;

/**
 * Controller for subjects.
 */
@Path("/subjects")
@Protected
public class SubjectController {
    private final SubjectService subjectService = new SubjectService();
    private final ExamService examService = new ExamService();
    private final UserService userService = new UserService();

    /**
     * Handles the fetching of all subjects from the database.
     *
     * @param securityContext provides access to user role.
     * @return list of all subjects in the database.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Subject> getSubjects(
        @Context SecurityContext securityContext
    ) {
        if (securityContext.isUserInRole("student")) {
            return subjectService.getSubjectsForStudent(
                UUID.fromString(
                    securityContext
                    .getUserPrincipal()
                    .getName()
                )
            );
        } else if (securityContext.isUserInRole("instructor")) {
            return subjectService.getSubjectsForInstructor(
                UUID.fromString(
                    securityContext
                    .getUserPrincipal()
                    .getName()
                )
            );
        } else {
            return subjectService.getSubjects();
        }
    }

    /**
     * Fetches a subject for a given slug.
     *
     * @param slug subject's slug
     * @return subject
     */
    @Path("/{slug}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Subject getSubject(@PathParam("slug") String slug) {
        return subjectService.getSubject(slug);
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
    public Subject createSubject(CreateSubjectDTO dto) {
        return subjectService.createSubject(
            dto.name,
            dto.description,
            dto.instructors,
            dto.students
        );
    }

    /**
     * Handles the fetching of all exams from the database.
     *
     * @param securityContext User details
     * @param subjectId ID of the subject to create the exam for
     * @return list of all exams for the subject
     */
    @Path("/{subjectId}/exams")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exam> getExamsForSubject(
        @Context SecurityContext securityContext,
        @PathParam("subjectId") String subjectId
    ) {
        if (securityContext.isUserInRole("student")) {
            return examService.getPublishedExamsForSubject(
                UUID.fromString(subjectId)
            );
        } else {
            return examService.getExamsForSubject(UUID.fromString(subjectId));
        }
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
    public Exam createExam(
        @PathParam("subjectId") String subjectId,
        CreateExamDTO dto
    ) {
        return examService.createExam(
            dto.name,
            dto.description,
            dto.startTime,
            dto.finishTime,
            UUID.fromString(subjectId)
        );
    }

    /**
     * Update a subject.
     *
     * @param subjectId subject's id
     * @param dto subject DTO
     * @return response to the client
     */
    @Path("/{subjectId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Subject updateSubject(
        @PathParam("subjectId") String subjectId,
        CreateSubjectDTO dto
    ) {
        return subjectService.updateSubject(
            dto.name,
            dto.description,
            UUID.fromString(subjectId)
        );
    }

    /**
     * Fetch instructors for a given subject.
     *
     * @param subjectId subject's id
     * @return list of instructors
     */
    @Path("/{subjectId}/instructors")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Instructor> getInstructors(
        @PathParam("subjectId") String subjectId
    ) {
        return userService.getInstructorsForSubject(UUID.fromString(subjectId));
    }

    /**
     * Adds given instructor to given subject.
     *
     * @param subjectId subject's id
     * @param instructorId instructor's id
     * @return response to client
     */
    @Path("/{subjectId}/instructors/{instructorId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addInstructorToSubject(
        @PathParam("subjectId") String subjectId,
        @PathParam("instructorId") String instructorId
    ) {
        subjectService.addInstructorToSubject(
            UUID.fromString(subjectId),
            UUID.fromString(instructorId)
        );

        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Adds given student to given subject.
     *
     * @param subjectId subject's id
     * @param studentId student's id
     * @return response to client
     */
    @Path("/{subjectId}/students/{studentId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudentToSubject(
        @PathParam("subjectId") String subjectId,
        @PathParam("studentId") String studentId
    ) {
        subjectService.addStudentToSubject(
            UUID.fromString(subjectId),
            UUID.fromString(studentId)
        );

        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Delete given instructor from given subject.
     *
     * @param subjectId subject's id
     * @param instructorId instructor's id
     * @return response to the client
     */
    @Path("/{subjectId}/instructors/{instructorId}")
    @DELETE
    public Response deleteInstructorFromSubject(
        @PathParam("subjectId") String subjectId,
        @PathParam("instructorId") String instructorId
    ) {
        subjectService.deleteInstructorFromSubject(
            UUID.fromString(subjectId),
            UUID.fromString(instructorId)
        );
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * Fetch students for a given subject.
     *
     * @param subjectId subject's id
     * @return list of students
     */
    @Path("/{subjectId}/students")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getStudents(
        @PathParam("subjectId") String subjectId
    ) {
        return userService.getStudentsForSubject(UUID.fromString(subjectId));
    }

    /**
     * Delete given instructor from given subject.
     *
     * @param subjectId subject's id
     * @param studentId instructor's id
     * @return response to the client
     */
    @Path("/{subjectId}/students/{studentId}")
    @DELETE
    public Response deleteStudentFromSubject(
        @PathParam("subjectId") String subjectId,
        @PathParam("studentId") String studentId
    ) {
        subjectService.deleteStudentFromSubject(
            UUID.fromString(subjectId),
            UUID.fromString(studentId)
        );
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
