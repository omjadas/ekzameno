package com.ekzameno.ekzameno.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                            klass.getSimpleName() + "Mapper")
                    .getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Find a model for a given ID.
     *
     * @param id ID of the model to find
     * @return model with the given ID
     * @throws SQLException if unable to retrieve the model
     */
    public T find(UUID id) throws SQLException {
        IdentityMap identityMap = IdentityMap.getInstance();
        T obj = (T) identityMap.get(id);

        if (obj != null) {
            return obj;
        }

        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, id);
            try (ResultSet rs = statement.executeQuery();) {
                rs.next();
                obj = load(rs);
                identityMap.put(id, obj);
                return obj;
            }
        }
    }

    /**
     * Find all models of a given type.
     *
     * @return all models of a given type
     * @throws SQLException if unable to retrieve the models
     */
    public List<T> findAll() throws SQLException {
        IdentityMap identityMap = IdentityMap.getInstance();
        String query = "SELECT * FROM " + getTableName();

        Connection connection = DBConnection.getInstance().getConnection();

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
     * Insert the given model.
     *
     * @param obj model to insert.
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
     * @param obj model to delete.
     * @throws SQLException if unable to delete the model
     */
    public void delete(T obj) throws SQLException {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";

        Connection connection = DBConnection.getInstance().getConnection();

        try (
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, obj.getId());
            statement.executeUpdate();
            IdentityMap.getInstance().remove(obj.getId());
        }
    }

    protected abstract T load(ResultSet rs) throws SQLException;

    protected abstract String getTableName();
}
