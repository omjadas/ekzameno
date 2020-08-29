package com.ekzameno.ekzameno.models;

import java.util.UUID;

public class Student extends User {
    public static final String TYPE = "STUDENT";

    public Student(UUID id, String email, String name, String passwordHash) {
        super(email, name, passwordHash);
        setId(id);
    }

    public Student(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }
}
