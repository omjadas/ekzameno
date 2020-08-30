package com.ekzameno.ekzameno.shared;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ekzameno.ekzameno.mappers.Mapper;
import com.ekzameno.ekzameno.models.Model;

public class UnitOfWork {
    private static ThreadLocal<UnitOfWork> current = new ThreadLocal<>();

    private List<Model> newObjects = new ArrayList<>();
    private List<Model> dirtyObjects = new ArrayList<>();
    private List<Model> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public static UnitOfWork getCurrent() {
        return current.get();
    }

    private boolean oneContains(Model obj) {
        return dirtyObjects.contains(obj) || deletedObjects.contains(obj) || newObjects.contains(obj);
    }

    public void registerNew(Model obj) {
        if (oneContains(obj)) {
            return;
        }

        newObjects.add(obj);
    }

    public void registerDirty(Model obj) {
        if (oneContains(obj)) {
            return;
        }

        dirtyObjects.add(obj);
    }

    public void registerDeleted(Model obj) {
        if (newObjects.remove(obj)) {
            return;
        }

        dirtyObjects.remove(obj);
        deletedObjects.add(obj);
    }

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
    }
}
