package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import com.ekzameno.ekzameno.models.Model;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;

/**
 * Abstract Data Mapper class.
 *
 * @param <T> type of model the Data Mapper is for.
 */
public abstract class Mapper<T extends Model> {
    /**
     * Retrieve a Data Mapper for a given class.
     *
     * @param <T>   generic type for the mapper
     * @param klass class to retrieve a mapper for
     * @return mapper for the given class
     */
    public static <T extends Model> Mapper<T> getMapper(Class<?> klass) {
        try {
            return (Mapper<T>) Class
                .forName(
                    "com.ekzameno.ekzameno.mappers." +
                        klass.getSimpleName() + "Mapper"
                )
                .getDeclaredConstructor()
                .newInstance();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Find a model for a given ID.
     *
     * @param id        ID of the model to find
     * @param forUpdate whether the row should be locked
     * @return model with the given ID
     * @throws SQLException if unable to retrieve the model
     */
    public T findById(
        UUID id,
        boolean forUpdate
    ) throws NotFoundException, SQLException {
        IdentityMap identityMap = IdentityMap.getCurrent();
        T obj = (T) identityMap.get(id);

        if (obj != null) {
            return obj;
        }

        return findByProp("id", id, forUpdate);
    }

    /**
     * Find a model for a given ID.
     *
     * @param id ID of the model to find
     * @return model with the given ID
     * @throws SQLException if unable to retrieve the model
     */
    public T findById(UUID id) throws SQLException, NotFoundException {
        return findById(id, false);
    }

    protected T findByProp(String prop, Object value, boolean forUpdate)
        throws SQLException, NotFoundException {
        String query = "SELECT * FROM " + getTableName() +
            " WHERE " + prop + " = ?" + (forUpdate ? " FOR UPDATE" : "");
        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, value);
            try (ResultSet rs = statement.executeQuery();) {
                if (rs.next()) {
                    T obj = load(rs);
                    IdentityMap.getCurrent().put(obj.getId(), obj);
                    return obj;
                } else {
                    throw new NotFoundException();
                }
            }
        }
    }

    /**
     * Find all models of a given type.
     *
     * @param forUpdate whether the rows should be locked
     * @return all models of a given type
     * @throws SQLException if unable to retrieve the models
     */
    public List<T> findAll(boolean forUpdate) throws SQLException {
        IdentityMap identityMap = IdentityMap.getCurrent();
        String query = "SELECT * FROM " + getTableName() +
            (forUpdate ? " FOR UPDATE" : "");

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
        ) {
            List<T> objects = new ArrayList<>();

            while (rs.next()) {
                T obj = load(rs);
                identityMap.put(obj.getId(), obj);
                objects.add(obj);
            }

            return objects;
        }
    }

    /**
     * Find all models of a given type.
     *
     * @return all models of a given type
     * @throws SQLException if unable to retrieve the models
     */
    public List<T> findAll() throws SQLException {
        return findAll(false);
    }

    /**
     * Insert the given model.
     *
     * @param obj model to insert
     * @throws SQLException if unable to insert the model
     */
    public abstract void insert(T obj) throws SQLException;

    /**
     * Update the given model.
     *
     * @param obj model to update
     * @throws SQLException if unable to update the model
     */
    public abstract void update(T obj) throws SQLException;

    /**
     * Delete the given model.
     *
     * @param obj model to delete
     * @throws SQLException if unable to delete the model
     */
    public void delete(T obj) throws SQLException {
        deleteById(obj.getId());
    }

    /**
     * Delete the model matching the given ID.
     *
     * @param id id of the model to delete
     * @throws SQLException if unable to delete the model
     */
    public void deleteById(UUID id) throws SQLException {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";

        Connection connection = DBConnection.getCurrent().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, id);
            statement.executeUpdate();
            IdentityMap.getCurrent().remove(id);
        }
    }

    protected abstract T load(ResultSet rs) throws SQLException;

    protected abstract String getTableName();
}
