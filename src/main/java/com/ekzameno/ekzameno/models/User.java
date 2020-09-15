package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Users of the system.
 */
public abstract class User extends Model {
    private String email;
    private String name;
    @JsonIgnore
    private String passwordHash;
    private String type;

    /**
     * Create a User with an ID.
     *
     * @param id           ID of the User
     * @param email        email address of the User
     * @param name         name of the User
     * @param passwordHash password hash of the user
     * @param type         type of the user
     */
    public User(
        UUID id,
        String email,
        String name,
        String passwordHash,
        String type
    ) {
        super(id);
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
        this.type = type;
    }

    /**
     * Create a User without an ID (registers as new).
     *
     * @param email        email address of the User
     * @param name         name of the User
     * @param passwordHash password hash of the user
     * @param type         type of the user
     */
    public User(String email, String name, String passwordHash, String type) {
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
        this.type = type;
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

    public String getType() {
        return type;
    }

    /**
     * Set the email of the User (marks the User as dirty).
     *
     * @param email email of the User
     */
    public void setEmail(String email) {
        this.email = email;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the name of the User (marks the User as dirty).
     *
     * @param name name of the User
     */
    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Set the password hash of the User (marks the User as dirty).
     *
     * @param passwordHash password hash of the user
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        UnitOfWork.getCurrent().registerDirty(this);
    }
}
