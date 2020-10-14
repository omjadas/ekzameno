package com.ekzameno.ekzameno.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ekzameno.ekzameno.exceptions.ConflictException;

/**
 * Mapper for ConflictException.
 */
@Provider
public class ConflictExceptionMapper
    implements ExceptionMapper<ConflictException> {
    @Override
    public Response toResponse(ConflictException e) {
        return Response
            .status(Response.Status.CONFLICT)
            .build();
    }
}
