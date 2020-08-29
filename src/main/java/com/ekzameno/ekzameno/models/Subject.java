package com.ekzameno.ekzameno.models;

import java.util.List;
import java.util.UUID;

public class Subject extends Model {
    private String name;

    public Subject(UUID id, String name) {
        super(id);
        this.name = name;
    }

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
