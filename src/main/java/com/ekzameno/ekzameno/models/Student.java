package com.ekzameno.ekzameno.models;

import java.util.List;
import java.util.UUID;

public class Student extends User {
    public static final String TYPE = "STUDENT";

    public Student(UUID id, String email, String name, String passwordHash) {
        super(id, email, name, passwordHash);
    }

    public Student(String email, String name, String passwordHash) {
        super(email, name, passwordHash);
    }

    public List<Subject> getSubjects() {
        return null;
    }
}
