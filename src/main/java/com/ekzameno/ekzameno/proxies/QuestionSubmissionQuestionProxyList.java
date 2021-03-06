package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.QuestionSubmissionMapper;
import com.ekzameno.ekzameno.models.QuestionSubmission;

/**
 * Proxy list for QuestionSubmissions owned by Questions.
 */
public class QuestionSubmissionQuestionProxyList
        extends ProxyList<QuestionSubmission> {
    /**
     * Create a QuestionSubmissionQuestionProxyList.
     *
     * @param questionId ID of the question the questions submissions are for
     */
    public QuestionSubmissionQuestionProxyList(UUID questionId) {
        super(questionId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new QuestionSubmissionMapper().findAllForQuestion(id);
        }
    }
}
