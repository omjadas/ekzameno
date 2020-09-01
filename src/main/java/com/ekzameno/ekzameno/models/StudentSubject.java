package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;

/**
 * Associative table for Students and Subjects.
 */
public class StudentSubject extends Model {
    private UUID studentId;
    private UUID subjectId;
    private Student student = null;
    private Subject subject = null;

    public StudentSubject(UUID id, UUID studentId, UUID subjectId) {
        super(id);
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public Student getStudent() throws SQLException {
        if (student == null) {
            student = new StudentMapper().find(studentId);
        }
        return student;
    }

    public Subject getSubject() throws SQLException {
        if (subject == null) {
            subject = new SubjectMapper().find(subjectId);
        }
        return subject;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
        this.student = null;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
        this.student = null;
    }

    public void setStudent(Student student) {
        this.student = student;
        this.studentId = student.getId();
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subjectId = subject.getId();
    }
}
