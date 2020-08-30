package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.UnitOfWork;

public abstract class Model {
    private UUID id;

    /**
     * Create a model without an ID (registers as new).
     */
    public Model() {
        UnitOfWork.getCurrent().registerNew(this);;
    }

    /**
     * Create a model with an ID.
     *
     * @param id ID of the model
     */
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
