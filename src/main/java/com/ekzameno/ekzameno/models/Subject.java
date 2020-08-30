package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.mappers.InstructorMapper;
import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

public class Subject extends Model {
    private String name;
    private List<Instructor> instructors = null;
    private List<Student> students = null;
    private List<Exam> exams = null;

    public Subject(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Instructor> getInstructors() throws SQLException {
        if (instructors == null) {
            return new InstructorMapper().findAllForSubject(getId());
        } else {
            return instructors;
        }
    }

    public List<Student> getStudents() throws SQLException {
        if (students == null) {
            return new StudentMapper().findAllForSubject(getId());
        } else {
            return students;
        }
    }

    public List<Exam> getExams() throws SQLException {
        if (exams == null) {
            return new ExamMapper().findAllForSubject(getId());
        } else {
            return exams;
        }
    }

    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
