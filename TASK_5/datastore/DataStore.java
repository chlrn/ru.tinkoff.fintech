package com.example.rutinkofffintech.TASK_5.datastore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore<T> {

    private final Map<String, T> store = new ConcurrentHashMap<>();

    public T get(String id) {
        return store.get(id);
    }

    public void save(String id, T entity) {
        if (id == null || entity == null) {
            throw new IllegalArgumentException("ID and entity cannot be null");
        }
        store.put(id, entity);
    }

    public void update(String id, T entity) {
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("No entity found with the given ID: " + id);
        }
        store.put(id, entity);
    }

    public void delete(String id) {
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("No entity found with the given ID: " + id);
        }
        store.remove(id);
    }

    public Map<String, T> getAll() {
        return Map.copyOf(store); // Возвращает неизменяемую копию
    }
}
