package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.models.Exam;

/**
 * Proxy list for Exams.
 */
public class ExamProxyList extends ProxyList<Exam> {
    public ExamProxyList(UUID subjectId) {
        super(subjectId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new ExamMapper().findAllForSubject(id);
        }
    }
}
