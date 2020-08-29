package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Administrator extends User {
    public static final String TYPE = "ADMINISTRATOR";

    public Administrator(UUID id, String name, String passwordHash) {
        super(name, passwordHash);
        this.setId(id);
    }

    public Administrator(String name, String passwordHash) {
        super(name, passwordHash);
    }
}
