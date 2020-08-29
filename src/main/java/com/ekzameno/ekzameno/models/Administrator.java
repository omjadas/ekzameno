package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Administrator extends User {
    public static final String TYPE = "ADMINISTRATOR";

    public Administrator(UUID id, String email, String name, String passwordHash) {
        super(id, email, name, passwordHash);
    }

    public Administrator(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }
}
