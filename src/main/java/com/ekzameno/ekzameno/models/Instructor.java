package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Instructor extends User {
    public static final String TYPE = "INSTRUCTOR";

    public Instructor(UUID id, String name, String passwordHash) {
        super(name, passwordHash);
        setId(id);
    }

    public Instructor(String name, String passwordHash) {
        super(name, passwordHash);
    }
}
