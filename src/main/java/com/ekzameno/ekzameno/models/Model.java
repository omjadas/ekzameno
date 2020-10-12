package com.ekzameno.ekzameno.models;

import java.util.UUID;

import com.ekzameno.ekzameno.shared.IdentityMap;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Base Model class.
 */
public abstract class Model {
    private final UUID id;

    /**
     * Create a model without an ID (registers as new).
     */
    public Model() {
        this.id = UUID.randomUUID();
        UnitOfWork.getCurrent().registerNew(this);
        IdentityMap.getCurrent().put(id, this);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Model other = (Model) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
