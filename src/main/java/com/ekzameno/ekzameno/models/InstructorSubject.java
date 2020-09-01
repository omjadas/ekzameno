package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.InstructorMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;

public class InstructorSubject extends Model {
    private UUID instructorId;
    private UUID subjectId;
    private Instructor instructor = null;
    private Subject subject = null;

    public InstructorSubject(UUID id, UUID instructorId, UUID subjectId) {
        super(id);
        this.instructorId = instructorId;
        this.subjectId = subjectId;
    }

    public UUID getInstructorId() {
        return instructorId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public Instructor getInstructor() throws SQLException {
        if (instructor == null) {
            instructor = new InstructorMapper().find(instructorId);
        }
        return instructor;
    }

    public Subject getSubject() throws SQLException {
        if (subject == null) {
            subject = new SubjectMapper().find(subjectId);
        }
        return subject;
    }

    public void setInstructorId(UUID instructorId) {
        this.instructorId = instructorId;
        this.instructor = null;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
        this.instructor = null;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
        this.instructorId = instructor.getId();
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subjectId = subject.getId();
    }
}
