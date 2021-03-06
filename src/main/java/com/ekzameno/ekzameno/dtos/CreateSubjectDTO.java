package com.ekzameno.ekzameno.dtos;

import java.util.UUID;

/**
 * DTO for creating subjects.
 */
public class CreateSubjectDTO {
    public String name;
    public String description;
    public UUID[] instructors;
    public UUID[] students;
}
