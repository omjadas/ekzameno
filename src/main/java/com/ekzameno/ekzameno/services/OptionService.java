package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.InternalServerErrorException;

import com.ekzameno.ekzameno.mappers.OptionMapper;
import com.ekzameno.ekzameno.models.Option;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service for managing options.
 */
public class OptionService {
    private final OptionMapper optionMapper = new OptionMapper();

    /**
     * Create an option for a given question.
     *
     * @param answer     answer for the option
     * @param correct    whether the option is correct
     * @param questionId ID of the question to create the option in
     * @return created option
     */
    public Option createOption(
        String answer,
        boolean correct,
        UUID questionId
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            Option option = new Option(answer, correct, questionId);
            UnitOfWork.getCurrent().commit();
            return option;
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update the given option.
     *
     * @param optionId ID of the option to update
     * @param answer   new answer for the option
     * @param correct  whether the option is correct
     * @return updated option
     */
    public Option updateOption(
        UUID optionId,
        String answer,
        boolean correct
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            Option option = optionMapper.findById(optionId);
            option.setAnswer(answer);
            option.setCorrect(correct);
            UnitOfWork.getCurrent().commit();
            return option;
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Delete the given option.
     *
     * @param optionId ID of the option to update
     */
    public void deleteOption(UUID optionId) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            optionMapper.deleteById(optionId);
            UnitOfWork.getCurrent().commit();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Get options for a given question.
     *
     * @param questionId ID of the question to retrieve options for
     * @return options
     */
    public List<Option> getOptionsForQuestion(UUID questionId) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            return optionMapper.findAllForQuestion(questionId);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
