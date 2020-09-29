package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.UUID;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.models.Option;
import com.ekzameno.ekzameno.shared.DBConnection;

/**
 * Service for managing options.
 */
public class OptionService {
    public Option createOption(
        String answer,
        boolean correct,
        UUID questionId
    ) {
        try (DBConnection connection = DBConnection.getInstance()) {
            return new Option(answer, correct, questionId);
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                throw new NotFoundException();
            }

            throw new InternalServerErrorException();
        }
    }
}
