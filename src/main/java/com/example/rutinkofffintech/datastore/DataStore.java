package com.example.rutinkofffintech.datastore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore<T> {

    private final Map<String, T> store = new ConcurrentHashMap<>();

    public T get(String id) {
        return store.get(id);
    }

    public void save(String id, T entity) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        store.put(id, entity);
    }

    public void update(String id, T entity) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("No entity found with the given ID: " + id);
        }
        store.put(id, entity);
    }


    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("No entity found with the given ID: " + id);
        }
        store.remove(id);
    }
    public boolean exists(String id) {
        return store.containsKey(id);
    }



    public Map<String, T> getAll() {
        return new ConcurrentHashMap<>(store); // Возвращаем изменяемую копию

    }
}
