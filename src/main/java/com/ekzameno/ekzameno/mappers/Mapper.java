package com.ekzameno.ekzameno.mappers;

import java.util.List;
import java.util.UUID;

public abstract class Mapper<T> {
    public abstract T find(UUID id);

    public abstract List<T> findAll();

    public abstract void save(T obj);

    public abstract void delete(T obj);
}
