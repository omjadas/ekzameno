package com.ekzameno.ekzameno.services;

import java.sql.SQLException;

import com.ekzameno.ekzameno.mappers.UserMapper;
import com.ekzameno.ekzameno.models.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Service to handle authentication.
 */
public class AuthService {
    private UserMapper userMapper = new UserMapper();

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
