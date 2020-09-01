package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.models.ExamSubmission;

/**
 * Proxy list for ExamSubmissions owned by Students.
 */
public class ExamSubmissionStudentProxyList extends ProxyList<ExamSubmission> {
    public ExamSubmissionStudentProxyList(UUID id) {
        super(id);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            new ExamSubmissionMapper().findAllForStudent(id);
        }
    }
}
