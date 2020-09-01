package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.Mapper;
import com.ekzameno.ekzameno.models.Model;

public abstract class ProxyList<T extends Model> {
    protected UUID id;
    protected List<T> models = null;

    public ProxyList(UUID id) {
        this.id = id;
    }

    public void add(T obj) throws SQLException {
        init();
        models.add(obj);
    }

    public boolean contains(T obj) throws SQLException {
        init();
        return models.contains(obj);
    }

    public T get(int index) throws SQLException {
        init();
        return models.get(index);
    }

    public int size() throws SQLException {
        init();
        return models.size();
    }

    public void remove(T obj) throws SQLException {
        Mapper.getMapper(obj.getClass()).delete(obj);
    }

    protected abstract void init() throws SQLException;
}
