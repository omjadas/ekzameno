package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.models.Model;

public abstract class Mapper<T extends Model> {
    public abstract T find(UUID id) throws SQLException;

    public abstract List<T> findAll() throws SQLException;

    public void save(T obj) throws SQLException {
        if (obj.getId() == null) {
            insert(obj);
        } else {
            update(obj);
        }
    }

    public abstract void insert(T obj) throws SQLException;

    public abstract void update(T obj) throws SQLException;

    public abstract void delete(T obj) throws SQLException;

    protected abstract T load(ResultSet rs) throws SQLException;
}
