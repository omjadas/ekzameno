package com.ekzameno.ekzameno.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Model;

/**
 * IdentityMap used to cache models in memory.
 */
public class IdentityMap {
    private static IdentityMap identityMap = new IdentityMap();

    private Map<UUID, Model> map = new HashMap<>();

    private IdentityMap() { }

    /**
     * Retrieve singleton IdentityMap.
     *
     * @return singleton IdentityMap
     */
    public static IdentityMap getInstance() {
        return identityMap;
    }

    /**
     * Retrieve a model from the IdentityMap for a given ID.
     *
     * @param id ID of the model to retrieve
     * @return the requested model
     */
    public Model get(UUID id) {
        return map.get(id);
    }

    /**
     * Put a model into the IdentityMap.
     *
     * @param id ID of the model to put
     * @param obj model to put into the IdentityMap
     */
    public void put(UUID id, Model obj) {
        map.put(id, obj);
    }

    /**
     * Remove a model from the IdentityMap.
     *
     * @param id ID of the model to remove
     */
    public void remove(UUID id) {
        map.remove(id);
    }
}
