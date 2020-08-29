package com.ekzameno.ekzameno.models;

public abstract class User extends Model {
    private String name;
    private String passwordHash;

    public User(String name, String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
