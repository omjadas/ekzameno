package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.models.Question;

public class QuestionProxyList extends ProxyList<Question> {
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
