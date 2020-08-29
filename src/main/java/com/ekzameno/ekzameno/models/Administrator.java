package com.ekzameno.ekzameno.models;

public class Administrator extends User {
    public static final String TYPE = "ADMINISTRATOR";

    public Administrator(String name, String passwordHash) {
        super(name, passwordHash);
    }
}
