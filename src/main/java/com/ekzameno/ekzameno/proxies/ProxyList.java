package com.ekzameno.ekzameno.proxies;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.Mapper;
import com.ekzameno.ekzameno.models.Model;

/**
 * A Proxy around a list of models.
 * @param <T> type of the models
 */
public abstract class ProxyList<T extends Model> {
    protected final UUID id;
    protected List<T> models = null;

    /**
     * Create a proxy list with a given ID.
     *
     * @param id ID of the model the list belongs to
     */
    public ProxyList(UUID id) {
        this.id = id;
    }

    /**
     * Add a model to the list.
     *
     * @param obj model to add to the list
     * @throws SQLException if unable to retrieve the models
     */
    public void add(T obj) throws SQLException {
        init();
        models.add(obj);
    }

    /**
     * Check if the list contains a given model.
     *
     * @param obj model to check in the list
     * @return true if the list contains the model, false otherwise
     * @throws SQLException if unable to retrieve the models
     */
    public boolean contains(T obj) throws SQLException {
        init();
        return models.contains(obj);
    }

    /**
     * Get the model from the list at the given index.
     *
     * @param index index of the model to get
     * @return model at the given index
     * @throws SQLException if unable to retrieve the models
     */
    public T get(int index) throws SQLException {
        init();
        return models.get(index);
    }

    /**
     * Get the size of the list.
     *
     * @return the size of the list
     * @throws SQLException if unable to retrieve the models
     */
    public int size() throws SQLException {
        init();
        return models.size();
    }

    /**
     * Remove a model from the list.
     *
     * @param obj model to remove from the list
     * @throws SQLException if unable to delete the model
     */
    public void remove(T obj) throws SQLException {
        Mapper.getMapper(obj.getClass()).delete(obj);
    }

    protected abstract void init() throws SQLException;
}
