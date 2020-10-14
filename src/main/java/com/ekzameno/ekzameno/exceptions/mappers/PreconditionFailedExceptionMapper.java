package com.ekzameno.ekzameno.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ekzameno.ekzameno.exceptions.PreconditionFailedException;

/**
 * Mapper for PreconditionFailedException.
 */
@Provider
public class PreconditionFailedExceptionMapper
    implements ExceptionMapper<PreconditionFailedException> {
    @Override
    public Response toResponse(PreconditionFailedException e) {
        return Response.status(Response.Status.PRECONDITION_FAILED)
            .build();
    }
}
