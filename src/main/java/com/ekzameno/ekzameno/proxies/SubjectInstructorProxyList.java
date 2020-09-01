package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.InstructorSubjectMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.models.Subject;

public class SubjectInstructorProxyList extends ProxyList<Subject> {
    public SubjectInstructorProxyList(UUID instructorId) {
        super(instructorId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new SubjectMapper().findAllForInstructor(id);
        }
    }

    @Override
    public void remove(Subject subject) throws SQLException {
        models.remove(subject);
        new InstructorSubjectMapper().deleteByRelationIds(id, subject.getId());
    }
}
