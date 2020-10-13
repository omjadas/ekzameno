package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.InstructorMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Associative table for Instructors and Subjects.
 */
public class InstructorSubject extends Model {
    private UUID instructorId;
    private UUID subjectId;
    @JsonIgnore
    private Instructor instructor = null;
    @JsonIgnore
    private Subject subject = null;

    /**
     * Create an InstructorSubject with an ID.
     *
     * @param id           ID of the InstructorSubject
     * @param instructorId ID of the instructor
     * @param subjectId    ID of the subject
     */
    public InstructorSubject(UUID id, UUID instructorId, UUID subjectId) {
        super(id);
        this.instructorId = instructorId;
        this.subjectId = subjectId;
    }

    /**
     * Create an InstructorSubject without an ID (registers as new).
     *
     * @param instructorId ID of the instructor
     * @param subjectId    ID of the subject
     */
    public InstructorSubject(UUID instructorId, UUID subjectId) {
        this.instructorId = instructorId;
        this.subjectId = subjectId;
    }

    public UUID getInstructorId() {
        return instructorId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    /**
     * Retrieve the associated Instructor.
     *
     * @return the associated instructor
     * @throws SQLException if unable to retrieve the instructor
     */
    public Instructor getInstructor() throws SQLException {
        if (instructor == null) {
            instructor = new InstructorMapper().findById(instructorId);
        }
        return instructor;
    }

    /**
     * Retrieve the associated Subject.
     *
     * @return the associated subject
     * @throws SQLException if unable to retrieve the subject
     */
    public Subject getSubject() throws SQLException {
        if (subject == null) {
            subject = new SubjectMapper().findById(subjectId);
        }
        return subject;
    }

    /**
     * Set the ID of the associated Instructor (marks the InstructorSubject as
     * dirty).
     *
     * @param instructorId ID of the associated instructor
     */
    public void setInstructorId(UUID instructorId) {
        this.instructorId = instructorId;
        this.instructor = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the ID of the associated subject (marks the InstructorSubject as
     * dirty).
     *
     * @param subjectId ID of the associated subject
     */
    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
        this.instructor = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the associated instructor (marks the InstructorSubject as dirty).
     *
     * @param instructor the associated instructor
     */
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
        this.instructorId = instructor.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the associated subject (marks the InstructorSubject as dirty).
     *
     * @param subject the associated subject
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subjectId = subject.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((instructorId == null)
            ? 0
            : instructorId.hashCode());
        result = prime * result + ((subjectId == null)
            ? 0
            : subjectId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InstructorSubject other = (InstructorSubject) obj;
        if (instructorId == null) {
            if (other.instructorId != null) {
                return false;
            }
        } else if (!instructorId.equals(other.instructorId)) {
            return false;
        }
        if (subjectId == null) {
            if (other.subjectId != null) {
                return false;
            }
        } else if (!subjectId.equals(other.subjectId)) {
            return false;
        }
        return true;
    }
}
