package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.models.Student;

public class StudentProxyList extends ProxyList<Student> {
    public StudentProxyList(UUID subjectId) {
        super(subjectId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new StudentMapper().findAllForSubject(id);
        }
    }
}
