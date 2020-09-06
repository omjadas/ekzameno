package com.ekzameno.ekzameno.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * Filter to determine whether requests are authenticated.
 */
@WebFilter("/api/*")
public class AuthFilter implements Filter {
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
                Jwts.parserBuilder()
                    .setSigningKey("abc")
                    .build()
                    .parseClaimsJws(jwt.get().getValue());
            } catch (JwtException ex) {
                ((HttpServletResponse) response).sendError(
                    HttpServletResponse.SC_UNAUTHORIZED
                );
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
