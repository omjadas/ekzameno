package com.ekzameno.ekzameno.filters;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.ekzameno.ekzameno.mappers.UserMapper;
import com.ekzameno.ekzameno.models.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Filter to determine whether requests are authenticated.
 */
@Provider
@Priority(0)
@Protected
public class AuthFilter implements ContainerRequestFilter {
    private final Key key = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(
            System.getenv(
                "JWT_SECRET"
            )
        )
    );

    @Override
    public void filter(
        ContainerRequestContext requestContext
    ) throws IOException {
        Cookie cookie = requestContext.getCookies().get("jwt");
        if (cookie != null) {
            String jwt = cookie.getValue();
            try {
                String subject = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();

                User user = new UserMapper().findById(UUID.fromString(subject));
                SecurityContext currentSecurityContext = requestContext
                    .getSecurityContext();

                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return () -> user.getId().toString();
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return user
                            .getType()
                            .toLowerCase()
                            .equals(role.toLowerCase());
                    }

                    @Override
                    public boolean isSecure() {
                        return currentSecurityContext.isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "JWT";
                    }
                });
            } catch (JwtException | SQLException e) {
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build()
                );
            }
        } else {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build()
            );
        }
    }
}
