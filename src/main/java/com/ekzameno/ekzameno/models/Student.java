package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Student extends User {
    public static final String TYPE = "STUDENT";

    public Student(UUID id, String name, String passwordHash) {
        super(name, passwordHash);
        this.setId(id);
    }

    public Student(String name, String passwordHash) {
        super(name, passwordHash);
    }
}
