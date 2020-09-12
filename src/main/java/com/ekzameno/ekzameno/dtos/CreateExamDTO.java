package com.ekzameno.ekzameno.dtos;

import org.glassfish.jersey.message.internal.OutboundMessageContext.StreamProvider;

/**
 * DTO for creating exams.
 */
public class CreateExamDTO {
    public String name;
    public String description;
    public Date publishDate;
    public Date closeDate;
}
