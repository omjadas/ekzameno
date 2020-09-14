package com.ekzameno.ekzameno.controllers;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.services.ExamService;

/**
 * Controller for Exams.
 */
@Path("/exams")
public class ExamController {
}
