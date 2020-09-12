package com.ekzameno.ekzameno.controllers;

import java.security.Key;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.SignInUserDTO;
import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.services.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Servlet implementation class LoginServlet.
 */
@Path("/auth")
public class AuthController {
    private AuthService authService = new AuthService();
    private Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(System.getenv(
        "JWT_SECRET"
    )));

    /**
     * Sign a user in to Ekzameno.
     *
     * @param dto DTO for user sign in
     * @return response
     */
    @Path("/signin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(SignInUserDTO dto) {
        User user = authService.authenticateUser(dto.email, dto.password);

        if (user != null) {
            String jwt = Jwts
                .builder()
                .setSubject(user.getId().toString())
                .signWith(key)
                .compact();

            NewCookie cookie = new NewCookie(
                "jwt",
                jwt,
                null,
                null,
                null,
                -1,
                false,
                true
            );
            return Response.ok().cookie(cookie).entity(user).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("/signout")
    @POST
    public Response signOut() {
        return Response
            .ok()
            .header(
                "set-cookie",
                "jwt=;Expires=Thu, 01-Jan-1970 00:00:01 GMT"
            )
            .build();
    }
}
