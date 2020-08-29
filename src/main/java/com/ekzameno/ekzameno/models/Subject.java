package com.ekzameno.ekzameno.models;

import java.util.List;

public class Subject extends Model {
    private String name;

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<User> getInstructors() {
        return null;
    }

    public List<User> getStudents() {
        return null;
    }
}
