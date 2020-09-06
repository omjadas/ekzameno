package com.ekzameno.ekzameno.services;

import java.sql.SQLException;

import com.ekzameno.ekzameno.mappers.UserMapper;
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
    private UserMapper userMapper = new UserMapper();

    /**
     * Register a user.
     *
     * @param name name of the user to register
     * @param email email address of the user to register
     * @param password password of the user to register
     * @param type type of the user to register
     * @return the new user
     */
    public User registerUser(
        String name,
        String email,
        String password,
        String type
    ) {
        User user;
        try {
            user = userMapper.findByEmail(email);
            return null;
        } catch (SQLException e) {
            String passwordHash = BCrypt.withDefaults().hashToString(
                12,
                password.toCharArray()
            );

            try (DBConnection connection = DBConnection.getInstance()) {
                if (type.equals("student")) {
                    user = new Student(email, name, passwordHash);
                } else if (type.equals("instructor")) {
                    user = new Instructor(email, name, passwordHash);
                } else if (type.equals("administrator")) {
                    user = new Administrator(email, name, passwordHash);
                } else {
                    return null;
                }

                UnitOfWork.getCurrent().commit();
                return user;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
