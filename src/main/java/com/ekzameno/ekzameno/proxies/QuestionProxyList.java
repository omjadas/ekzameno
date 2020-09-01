package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.models.Question;

/**
 * Proxy list for Questions.
 */
public class QuestionProxyList extends ProxyList<Question> {
    /**
     * Create a QuestionProxyList.
     *
     * @param examId ID of the exam the questions belong to
     */
    public QuestionProxyList(UUID examId) {
        super(examId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new QuestionMapper().findAllForExam(id);
        }
    }
}
