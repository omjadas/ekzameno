package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.InstructorSubjectMapper;
import com.ekzameno.ekzameno.mappers.SubjectMapper;
import com.ekzameno.ekzameno.models.Subject;

/**
 * Proxy list for Subjects owned by Instructors.
 */
public class SubjectInstructorProxyList extends ProxyList<Subject> {
    /**
     * Create a SubjectInstructorProxyList.
     *
     * @param instructorId ID of the instructor the subjects are taught by
     */
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
