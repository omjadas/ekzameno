package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.StudentSubjectMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.models.Subject;

/**
 * Proxy list for Subjects owned by Students.
 */
public class SubjectStudentProxyList extends ProxyList<Subject> {
    /**
     * Create a SubjectStudentProxyList.
     *
     * @param studentId ID of the student the subjects are being taken by
     */
    public SubjectStudentProxyList(UUID studentId) {
        super(studentId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new SubjectMapper().findAllForStudent(id);
        }
    }

    @Override
    public void remove(Subject subject) throws SQLException {
        models.remove(subject);
        new StudentSubjectMapper().deleteByRelationIds(id, subject.getId());
    }
}
