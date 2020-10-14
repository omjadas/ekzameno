package com.ekzameno.ekzameno.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.ekzameno.ekzameno.exceptions.ConflictException;

/**
 * Mapper for ConflictException.
 */
public class ConflictExceptionMapper
    implements ExceptionMapper<ConflictException> {
    @Override
    public Response toResponse(ConflictException e) {
        return Response.status(Response.Status.CONFLICT)
            .build();
    }
}
