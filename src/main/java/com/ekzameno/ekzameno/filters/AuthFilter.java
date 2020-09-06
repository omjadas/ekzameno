package com.ekzameno.ekzameno.filters;

import java.io.IOException;
import java.security.Key;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekzameno.ekzameno.mappers.UserMapper;
import com.ekzameno.ekzameno.models.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Filter to determine whether requests are authenticated.
 */
@WebFilter("/api/*")
public class AuthFilter implements Filter {
    private Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(System.getenv(
        "JWT_SECRET"
    )));

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        Optional<Cookie> jwt = Arrays
            .stream(cookies)
            .filter(c -> c.getName().equals("jwt"))
            .findAny();

        if (!jwt.isPresent()) {
            ((HttpServletResponse) response).sendError(
                HttpServletResponse.SC_UNAUTHORIZED
            );
            return;
        } else {
            try {
                String subject = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt.get().getValue())
                    .getBody()
                    .getSubject();

                User user = new UserMapper().findById(UUID.fromString(subject));
                request.setAttribute("user", user);
            } catch (JwtException | SQLException e) {
                ((HttpServletResponse) response).sendError(
                    HttpServletResponse.SC_UNAUTHORIZED
                );
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
