package com.ekzameno.ekzameno.models;

import java.util.UUID;

public abstract class Model {
    private UUID id;

    public Model() { }

    public Model(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
