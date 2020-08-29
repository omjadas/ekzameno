package com.ekzameno.ekzameno.models;

import java.util.UUID;

public abstract class Model {
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
