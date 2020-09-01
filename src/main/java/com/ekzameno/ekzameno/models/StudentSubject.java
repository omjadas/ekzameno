package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Associative table for Students and Subjects.
 */
public class StudentSubject extends Model {
    private UUID studentId;
    private UUID subjectId;
    private Student student = null;
    private Subject subject = null;

    /**
     * Create a StudentSubject with an ID.
     *
     * @param id        ID of the StudentSubject
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     */
    public StudentSubject(UUID id, UUID studentId, UUID subjectId) {
        super(id);
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    /**
     * Create a StudentSubject with an ID.
     *
     * @param studentId ID of the student
     * @param subjectId ID of the subject
     */
    public StudentSubject(UUID studentId, UUID subjectId) {
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
            student = new StudentMapper().find(studentId);
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
            subject = new SubjectMapper().find(subjectId);
        }
        return subject;
    }

    /**
     * Set the ID of the associated student (marks the StudentSubject as dirty).
     *
     * @param studentId ID of the associated student
     */
    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
        this.student = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the ID of the associated subject (marks the StudentSubject as dirty).
     *
     * @param subjectId ID of the associated subject
     */
    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
        this.student = null;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the associated Student (marks the StudentSubject as dirty).
     *
     * @param student the associated student
     */
    public void setStudent(Student student) {
        this.student = student;
        this.studentId = student.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the associated subject (marks the StudentSubject as dirty).
     *
     * @param subject the associated subject
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subjectId = subject.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
