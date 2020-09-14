package com.ekzameno.ekzameno.controllers;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateUserDTO;
import com.ekzameno.ekzameno.exceptions.UnknownUserTypeException;
import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.services.UserService;

@Path("/users")
public class UserController {
    private final UserService userService = new UserService();

    @POST
    public Response createUser(CreateUserDTO dto) {
        try {
            User user = userService.registerUser(
                dto.name,
                dto.email,
                dto.password,
                dto.type
            );

            if (user != null) {
                return Response
                    .status(Response.Status.CREATED)
                    .entity("")
                    .build();
            } else {
                return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
            }
        } catch (UnknownUserTypeException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
