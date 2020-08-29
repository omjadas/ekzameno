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

public abstract class Mapper<T extends Model> {
    public static <T extends Model> Mapper<T> getMapper(Class<?> modelClass) {
        try {
            return (Mapper<T>) Class
                .forName(
                    "com.ekzameno.ekzameno.mappers." +
                    modelClass.getSimpleName() +
                    "Mapper"
                )
                .getDeclaredConstructor()
                .newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public T find(UUID id) throws SQLException {
        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, id);
            try (ResultSet rs = statement.executeQuery();) {
                rs.next();
                return load(rs);
            }
        }
    }

    public List<T> findAll() throws SQLException {
        String query = "SELECT * FROM " + getTableName();

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
        ) {
            List<T> objects = new ArrayList<>();

            while (rs.next()) {
                objects.add(load(rs));
            }

            return objects;
        }
    }

    public void save(T obj) throws SQLException {
        if (obj.getId() == null) {
            insert(obj);
        } else {
            update(obj);
        }
    }

    public abstract void insert(T obj) throws SQLException;

    public abstract void update(T obj) throws SQLException;

    public void delete(T obj) throws SQLException {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setObject(1, obj.getId());
            statement.executeUpdate();
        }
    }

    protected abstract T load(ResultSet rs) throws SQLException;

    protected abstract String getTableName();
}
