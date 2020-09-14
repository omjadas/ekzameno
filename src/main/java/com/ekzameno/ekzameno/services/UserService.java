package com.ekzameno.ekzameno.services;

import java.sql.SQLException;

import com.ekzameno.ekzameno.exceptions.UnknownUserTypeException;
import com.ekzameno.ekzameno.models.Administrator;
import com.ekzameno.ekzameno.models.Instructor;
import com.ekzameno.ekzameno.models.Student;
import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.UnitOfWork;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Service to handle users.
 */
public class UserService {
    /**
     * Register a user.
     *
     * @param name name of the user to register
     * @param email email address of the user to register
     * @param password password of the user to register
     * @param type type of the user to register
     * @return the new user
     * @throws UnknownUserTypeException
     */
    public User registerUser(
        String name,
        String email,
        String password,
        String type
    ) throws UnknownUserTypeException {
        String passwordHash = BCrypt.withDefaults().hashToString(
            12,
            password.toCharArray()
        );

        try (DBConnection connection = DBConnection.getInstance()) {
            User user;

            if (type.equals("student")) {
                user = new Student(email, name, passwordHash);
            } else if (type.equals("instructor")) {
                user = new Instructor(email, name, passwordHash);
            } else if (type.equals("administrator")) {
                user = new Administrator(email, name, passwordHash);
            } else {
                throw new UnknownUserTypeException();
            }

            UnitOfWork.getCurrent().commit();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
