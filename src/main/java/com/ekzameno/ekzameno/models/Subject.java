package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.proxies.ExamProxyList;
import com.ekzameno.ekzameno.proxies.InstructorProxyList;
import com.ekzameno.ekzameno.proxies.ProxyList;
import com.ekzameno.ekzameno.proxies.StudentProxyList;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Subjects that Instructors teach and Students enrol in.
 */
public class Subject extends Model {
    private String name;
    private ProxyList<Instructor> instructors;
    private ProxyList<Student> students;
    private ProxyList<Exam> exams;

    /**
     * Create a Subject with an ID.
     *
     * @param id   ID of the Subject
     * @param name name of the Subject
     */
    public Subject(UUID id, String name) {
        super(id);
        this.name = name;
        this.instructors = new InstructorProxyList(id);
        this.students = new StudentProxyList(id);
        this.exams = new ExamProxyList(id);
    }

    /**
     * Create a Subject without an ID (registers as new).
     *
     * @param name name of the Subject
     */
    public Subject(String name) {
        this.name = name;
        this.instructors = new InstructorProxyList(getId());
        this.students = new StudentProxyList(getId());
        this.exams = new ExamProxyList(getId());
    }

    public String getName() {
        return name;
    }

    /**
     * Retrieve the Instructors for the Subject.
     *
     * @return Instructors for the Subject
     */
    public ProxyList<Instructor> getInstructors() {
        return instructors;
    }

    /**
     * Retrieve the Students for the Subject.
     *
     * @return Students enrolled in the Subject
     */
    public ProxyList<Student> getStudents() {
        return students;
    }

    /**
     * Retrieve the Exams for the Subject.
     *
     * @return Exams created for the Subject
     */
    public ProxyList<Exam> getExams() {
        return exams;
    }

    /**
     * Set the name of the Subject (registers the Subject as dirty).
     *
     * @param name name of the Subject
     */
    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
