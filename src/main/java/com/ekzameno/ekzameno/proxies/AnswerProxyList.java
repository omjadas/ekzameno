package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.AnswerMapper;
import com.ekzameno.ekzameno.models.Answer;

public class AnswerProxyList extends ProxyList<Answer> {
    public AnswerProxyList(UUID questionId) {
        super(questionId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new AnswerMapper().findAllForQuestion(id);
        }
    }
}
