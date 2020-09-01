package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.models.ExamSubmission;

/**
 * Proxy list for ExamSubmissions owned by Students.
 */
public class ExamSubmissionStudentProxyList extends ProxyList<ExamSubmission> {
    /**
     * Create an ExamSubmissionStudentProxyList.
     *
     * @param studentId ID of the student the exam submissions belong to
     */
    public ExamSubmissionStudentProxyList(UUID studentId) {
        super(studentId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            new ExamSubmissionMapper().findAllForStudent(id);
        }
    }
}
