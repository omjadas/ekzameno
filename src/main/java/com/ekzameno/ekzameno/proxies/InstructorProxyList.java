package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.InstructorMapper;
import com.ekzameno.ekzameno.mappers.InstructorSubjectMapper;
import com.ekzameno.ekzameno.models.Instructor;

/**
 * Proxy list for Instructors.
 */
public class InstructorProxyList extends ProxyList<Instructor> {
    /**
     * Create an InstructorProxyList.
     *
     * @param subjectId ID of the subject the instructors teach
     */
    public InstructorProxyList(UUID subjectId) {
        super(subjectId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new InstructorMapper().findAllForSubject(id);
        }
    }

    @Override
    public void remove(Instructor instructor) throws SQLException {
        models.remove(instructor);
        new InstructorSubjectMapper().deleteByRelationIds(
            instructor.getId(),
            id
        );
    }
}
