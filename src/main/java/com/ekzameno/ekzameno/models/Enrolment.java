package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Associative table for Students and Subjects.
 */
public class Enrolment extends Model {
    private UUID studentId;
    private UUID subjectId;
    @JsonIgnore
    private Student student = null;
    @JsonIgnore
    private Subject subject = null;

    /**
     * Create an Enrolment with an ID.
     *
     * @param id        ID of the enrolment
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     */
    public Enrolment(UUID id, UUID studentId, UUID subjectId) {
        super(id);
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    /**
     * Create an Enrolment.
     *
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     */
    public Enrolment(UUID studentId, UUID subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    /**
     * Retrieve the associated Student.
     *
     * @return the associated student
     * @throws SQLException if unable to retrieve the associated student
     */
    public Student getStudent() throws SQLException {
        if (student == null) {
            student = new StudentMapper().findById(studentId);
        }
        return student;
    }

    /**
     * Retrieve the associated Subject.
     *
     * @return the associated subject
     * @throws SQLException if unable to retrieve the associated subject
     */
    public Subject getSubject() throws SQLException {
        if (subject == null) {
            subject = new SubjectMapper().findById(subjectId);
        }
        return subject;
    }

    /**
     * Set the ID of the associated student (marks the Enrolment as dirty).
     *
     * @param studentId ID of the associated student
     */
    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
        this.student = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the ID of the associated subject (marks the Enrolment as dirty).
     *
     * @param subjectId ID of the associated subject
     */
    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
        this.student = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the associated Student (marks the Enrolment as dirty).
     *
     * @param student the associated student
     */
    public void setStudent(Student student) {
        this.student = student;
        this.studentId = student.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the associated subject (marks the Enrolment as dirty).
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
        result = prime * result + ((studentId == null)
            ? 0
            : studentId.hashCode());
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
        Enrolment other = (Enrolment) obj;
        if (studentId == null) {
            if (other.studentId != null) {
                return false;
            }
        } else if (!studentId.equals(other.studentId)) {
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
