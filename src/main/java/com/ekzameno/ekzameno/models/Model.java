package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.IdentityMap;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Base Model class.
 */
public abstract class Model {
    private UUID id;

    /**
     * Create a model without an ID (registers as new).
     */
    public Model() {
        this.id = UUID.randomUUID();
        UnitOfWork.getCurrent().registerNew(this);
        IdentityMap.getInstance().put(id, this);
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
