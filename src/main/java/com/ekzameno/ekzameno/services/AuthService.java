package com.ekzameno.ekzameno.services;

import java.sql.SQLException;

import javax.ws.rs.InternalServerErrorException;

import com.ekzameno.ekzameno.mappers.UserMapper;
import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Service to handle authentication.
 */
public class AuthService {
    private final UserMapper userMapper = new UserMapper();

    /**
     * Authenticate a user.
     *
     * @param email email of the user to validate
     * @param password password of the user to validate
     * @return whether the user is authenticated
     */
    public User authenticateUser(String email, String password) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            User user = userMapper.findByEmail(email);
            boolean authenticated = BCrypt
                .verifyer()
                .verify(password.toCharArray(), user.getPasswordHash())
                .verified;
            return authenticated ? user : null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        } finally {
            IdentityMap.reset();
        }
    }
}
