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
 * 
 */
public class AuthService {
    private UserMapper userMapper = new UserMapper();

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
            } catch (SQLException t) {
                t.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Authenticate a user.
     *
     * @param email email of the user to validate
     * @param password password of the user to validate
     * @return whether the user is authenticated
     */
    public User authenticateUser(String email, String password) {
        try {
            User user = userMapper.findByEmail(email);
            boolean authenticated = BCrypt
                .verifyer()
                .verify(password.toCharArray(), user.getPasswordHash())
                .verified;
            return authenticated ? user : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
