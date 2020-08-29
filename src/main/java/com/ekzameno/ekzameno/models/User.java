package com.ekzameno.ekzameno.models;

import java.util.UUID;

public abstract class User extends Model {
    private String email;
    private String name;
    private String passwordHash;

    public User(UUID id, String email, String name, String passwordHash) {
        super(id);
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public User(String email, String name, String passwordHash) {
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
