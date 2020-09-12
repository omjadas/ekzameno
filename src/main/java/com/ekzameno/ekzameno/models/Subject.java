package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.proxies.ExamProxyList;
import com.ekzameno.ekzameno.proxies.InstructorProxyList;
import com.ekzameno.ekzameno.proxies.ProxyList;
import com.ekzameno.ekzameno.proxies.StudentProxyList;
import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.slugify.Slugify;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Subjects that Instructors teach and Students enrol in.
 */
public class Subject extends Model {
    private String name;
    private String description;
    private String slug;
    @JsonIgnore
    private ProxyList<Instructor> instructors;
    @JsonIgnore
    private ProxyList<Student> students;
    @JsonIgnore
    private ProxyList<Exam> exams;

    /**
     * Create a Subject with an ID.
     *
     * @param id   ID of the Subject
     * @param name name of the Subject
     * @param description description of the Subject
     * @param slug slug for the Subject
     */
    public Subject(UUID id, String name, String description, String slug) {
        super(id);
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.instructors = new InstructorProxyList(id);
        this.students = new StudentProxyList(id);
        this.exams = new ExamProxyList(id);
    }

    /**
     * Create a Subject without an ID (registers as new).
     *
     * @param name name of the Subject
     * @param description description of the Subject
     */
    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
        this.slug = new Slugify().slugify(name) + "-" +
            RandomStringUtils.randomAlphanumeric(8);

        this.instructors = new InstructorProxyList(getId());
        this.students = new StudentProxyList(getId());
        this.exams = new ExamProxyList(getId());
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
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

    /**
     * Set the description of the Subject (register the subject as dirty).
     *
     * @param description description of the Subject
     */
    public void setDescription(String description) {
        this.description = description;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
