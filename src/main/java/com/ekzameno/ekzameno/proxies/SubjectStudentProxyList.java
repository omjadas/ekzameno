package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.StudentSubjectMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.models.Subject;

public class SubjectStudentProxyList extends ProxyList<Subject> {
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
