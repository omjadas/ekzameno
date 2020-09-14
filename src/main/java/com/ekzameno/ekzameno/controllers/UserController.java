package com.ekzameno.ekzameno.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ekzameno.ekzameno.dtos.CreateUserDTO;
import com.ekzameno.ekzameno.exceptions.UnknownUserTypeException;
import com.ekzameno.ekzameno.exceptions.UserAlreadyExistsException;
import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.services.UserService;

/**
 * Controller for Users.
 */
@Path("/users")
public class UserController {
    private final UserService userService = new UserService();

    /**
     * Create a user using the information provided in the DTO.
     *
     * @param dto create user DTO
     * @return response to the client.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
        } catch (UserAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
