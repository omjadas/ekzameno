package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.InstructorMapper;
import com.ekzameno.ekzameno.models.Instructor;

public class InstructorProxyList extends ProxyList<Instructor> {
    public InstructorProxyList(UUID subjectId) {
        super(subjectId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new InstructorMapper().findAllForSubject(id);
        }
    }
}
