package com.ekzameno.ekzameno.models;

public abstract class User extends Model {
    private String email;
    private String name;
    private String passwordHash;

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
