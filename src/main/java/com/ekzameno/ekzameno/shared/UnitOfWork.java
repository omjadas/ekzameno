package com.ekzameno.ekzameno.shared;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ekzameno.ekzameno.mappers.Mapper;
import com.ekzameno.ekzameno.models.Model;

/**
 * Keeps track of changes to models.
 */
public class UnitOfWork {
    private static final ThreadLocal<UnitOfWork> current = new ThreadLocal<>();
    private final List<Model> newObjects = new ArrayList<>();
    private final List<Model> dirtyObjects = new ArrayList<>();
    private final List<Model> deletedObjects = new ArrayList<>();

    /**
     * Create a new thread local UnitOfWork.
     */
    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    private static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    /**
     * Retrieve the thread local UnitOfWork.
     *
     * @return thread local UnitOfWork
     */
    public static UnitOfWork getCurrent() {
        UnitOfWork uow = current.get();
        if (uow == null) {
            uow = new UnitOfWork();
            setCurrent(uow);
        }
        return uow;
    }

    private boolean objectInAnyList(Model obj) {
        return (
            dirtyObjects.contains(obj) ||
            deletedObjects.contains(obj) ||
            newObjects.contains(obj));
    }

    /**
     * Register a new model.
     *
     * @param obj model to register
     */
    public void registerNew(Model obj) {
        if (objectInAnyList(obj)) {
            return;
        }

        newObjects.add(obj);
    }

    /**
     * Register a dirty model.
     *
     * @param obj model to register
     */
    public void registerDirty(Model obj) {
        if (objectInAnyList(obj)) {
            return;
        }

        dirtyObjects.add(obj);
    }

    /**
     * Register a deleted model.
     *
     * @param obj model to register
     */
    public void registerDeleted(Model obj) {
        if (newObjects.remove(obj)) {
            return;
        }

        dirtyObjects.remove(obj);
        deletedObjects.add(obj);
    }

    /**
     * Rollback the transaction.
     *
     * @throws SQLException if unable to rollback the transaction
     */
    public void rollback() throws SQLException {
        DBConnection.getCurrent().getConnection().rollback();
    }

    /**
     * Commit changes to models.
     *
     * @throws SQLException if unable to commit changes
     */
    public void commit() throws SQLException {
        for (Model obj : newObjects) {
            Mapper.getMapper(obj.getClass()).insert(obj);
        }

        for (Model obj : dirtyObjects) {
            Mapper.getMapper(obj.getClass()).update(obj);
        }

        for (Model obj : deletedObjects) {
            Mapper.getMapper(obj.getClass()).delete(obj);
        }

        DBConnection.getCurrent().getConnection().commit();
        current.set(null);
    }
}
