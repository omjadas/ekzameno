package com.ekzameno.ekzameno.models;

import java.util.UUID;

/**
 * Administrator of the system.
 */
public class Administrator extends User {
    public static final String TYPE = "ADMINISTRATOR";

    /**
     * Create an Administrator with an ID.
     *
     * @param id ID of the Administrator
     * @param email email address of the Administrator
     * @param name name of the Administrator
     * @param passwordHash password hash of the Administrator
     */
    public Administrator(
        UUID id,
        String email,
        String name,
        String passwordHash
    ) {
        super(id, email, name, passwordHash);
    }

    /**
     * Create an Administrator without an ID (registers as new).
     *
     * @param email email address of the Administrator
     * @param name name of the Administrator
     * @param passwordHash password hash of the Administrator
     */
    public Administrator(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }
}
