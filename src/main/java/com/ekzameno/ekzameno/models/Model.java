package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;

public abstract class Model {
    private UUID id;

    public Model() {
        UnitOfWork.getCurrent().registerNew(this);;
    }

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
