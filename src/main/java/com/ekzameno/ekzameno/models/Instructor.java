package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Instructor extends User {
    public static final String TYPE = "INSTRUCTOR";

    public Instructor(UUID id, String email, String name, String passwordHash) {
        super(email, name, passwordHash);
        setId(id);
    }

    public Instructor(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }
}
