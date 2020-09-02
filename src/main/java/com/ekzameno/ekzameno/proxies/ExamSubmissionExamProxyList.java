package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamSubmissionMapper;
import com.ekzameno.ekzameno.models.ExamSubmission;

/**
 * Proxy list for ExamSubmissions owned by Exams.
 */
public class ExamSubmissionExamProxyList extends ProxyList<ExamSubmission> {
    /**
     * Create an ExamSubmissionExamProxyList.
     *
     * @param examId ID of the exam the submissions are for
     */
    public ExamSubmissionExamProxyList(UUID examId) {
        super(examId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            new ExamSubmissionMapper().findAllForExam(id);
        }
    }
}
