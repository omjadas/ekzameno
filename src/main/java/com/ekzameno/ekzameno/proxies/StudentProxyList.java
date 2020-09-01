package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.EnrolmentMapper;
import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.models.Student;

/**
 * Proxy list for students.
 */
public class StudentProxyList extends ProxyList<Student> {
    /**
     * Create a StudentProxyList.
     *
     * @param subjectId ID of the subject the students are enrolled in
     */
    public StudentProxyList(UUID subjectId) {
        super(subjectId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new StudentMapper().findAllForSubject(id);
        }
    }

    @Override
    public void remove(Student student) throws SQLException {
        models.remove(student);
        new EnrolmentMapper().deleteByRelationIds(student.getId(), id);
    }
}
