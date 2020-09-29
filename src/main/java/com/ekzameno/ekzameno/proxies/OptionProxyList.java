package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.OptionMapper;
import com.ekzameno.ekzameno.models.Option;

/**
 * Proxy list for Options.
 */
public class OptionProxyList extends ProxyList<Option> {
    /**
     * Create an OptionProxyList.
     *
     * @param questionId ID of the question the options belong to
     */
    public OptionProxyList(UUID questionId) {
        super(questionId);
    }

    @Override
    protected void init() throws SQLException {
        if (models == null) {
            models = new OptionMapper().findAllForQuestion(id);
        }
    }
}
