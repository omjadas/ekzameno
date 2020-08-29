package com.ekzameno.ekzameno.models;

public class Instructor extends User {
    public static final String TYPE = "INSTRUCTOR";

    public Instructor(String name, String passwordHash) {
        super(name, passwordHash);
    }
}
