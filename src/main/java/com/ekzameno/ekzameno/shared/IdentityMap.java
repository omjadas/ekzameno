package com.ekzameno.ekzameno.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Model;

public class IdentityMap {
    private static IdentityMap identityMap = new IdentityMap();

    private Map<UUID, Model> map = new HashMap<>();

    private IdentityMap() { }

    public static IdentityMap getInstance() {
        return identityMap;
    }

    public Model get(UUID id) {
        return map.get(id);
    }

    public void put(UUID id, Model obj) {
        map.put(id, obj);
    }

    public void remove(UUID id) {
        map.remove(id);
    }
}
