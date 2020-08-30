package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;

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

    public void setEmail(String email) {
        this.email = email;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
