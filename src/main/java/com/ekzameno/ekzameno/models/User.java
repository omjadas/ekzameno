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
    private final String type;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((passwordHash == null)
            ? 0
            : passwordHash.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (passwordHash == null) {
            if (other.passwordHash != null) {
                return false;
            }
        } else if (!passwordHash.equals(other.passwordHash)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
