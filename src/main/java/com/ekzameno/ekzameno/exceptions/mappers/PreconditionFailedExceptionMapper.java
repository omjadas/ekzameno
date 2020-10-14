package com.ekzameno.ekzameno.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.ekzameno.ekzameno.exceptions.PreconditionFailedException;

/**
 * Mapper for PreconditionFailedException.
 */
public class PreconditionFailedExceptionMapper
    implements ExceptionMapper<PreconditionFailedException> {
    @Override
    public Response toResponse(PreconditionFailedException e) {
        return Response.status(Response.Status.PRECONDITION_FAILED)
            .build();
    }
}
