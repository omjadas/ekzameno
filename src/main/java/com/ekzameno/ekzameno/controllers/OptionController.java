package com.ekzameno.ekzameno.controllers;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateOptionDTO;
import com.ekzameno.ekzameno.filters.Protected;
import com.ekzameno.ekzameno.models.Option;
import com.ekzameno.ekzameno.services.OptionService;

/**
 * Controller for options.
 */
@Path("/options")
@Protected
public class OptionController {
    private final OptionService optionService = new OptionService();

    /**
     * Update the given option.
     *
     * @param optionId ID of the option to update
     * @param dto      option DTO
     * @param headers  request headers
     * @return updated option
     */
    @Path("/{optionId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Option updateOption(
        @PathParam("optionId") String optionId,
        @Context HttpHeaders headers,
        CreateOptionDTO dto
    ) {
        return optionService.updateOption(
            UUID.fromString(optionId),
            dto.answer,
            dto.correct,
            headers.getHeaderString("if-match")
        );
    }

    /**
     * Delete the given option.
     *
     * @param optionId ID of the option to delete
     * @return response to the client
     */
    @Path("/{optionId}")
    @DELETE
    public Response deleteOption(@PathParam("optionId") String optionId) {
        optionService.deleteOption(UUID.fromString(optionId));
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
