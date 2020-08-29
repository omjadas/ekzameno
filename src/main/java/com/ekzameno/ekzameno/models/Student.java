package com.ekzameno.ekzameno.models;

public class Student extends User {
    public static final String TYPE = "STUDENT";

    public Student(String name, String passwordHash) {
        super(name, passwordHash);
    }
}
