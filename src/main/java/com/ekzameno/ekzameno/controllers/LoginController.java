package com.ekzameno.ekzameno.controllers;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.services.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Servlet implementation class LoginServlet.
 */
@WebServlet("/auth/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService authService = new AuthService();

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        User user = authService.authenticateUser("email", "password");

        if (user != null) {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("abc"));
            String jwt = Jwts
                .builder()
                .setSubject(user.getId().toString())
                .signWith(key)
                .compact();
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
