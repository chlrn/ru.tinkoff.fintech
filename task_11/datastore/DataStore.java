package com.example.rutinkofffintech.task_11.datastore;

import com.example.rutinkofffintech.task_11.snapshot.Snapshot;
import com.example.rutinkofffintech.task_11.snapshot.SnapshotManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore<T> {
    private final Map<String, T> store = new ConcurrentHashMap<>();
    private final SnapshotManager<T> snapshotManager = new SnapshotManager<>();

    public T get(String id) {
        return store.get(id);
    }

    public void save(String id, T entity) {
        store.put(id, entity);
        snapshotManager.saveSnapshot(new Snapshot<>() {
            @Override
            public T restore() {
                return null;
            }
        }); // Создаем снимок
    }

    public void update(String id, T entity) {
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("No entity found with the given ID: " + id);
        }
        store.put(id, entity);
        snapshotManager.saveSnapshot(new Snapshot<>() {
            @Override
            public T restore() {
                return null;
            }
        }); // Создаем снимок
    }

    public void delete(String id) {
        T entity = store.remove(id);
        snapshotManager.saveSnapshot(new Snapshot<>() {
            @Override
            public T restore() {
                return null;
            }
        }); // Сохраняем состояние перед удалением
    }

    // Восстановление состояния из последнего снимка
    public void restoreLastSnapshot(String id) {
        Snapshot<T> lastSnapshot = snapshotManager.getSnapshot(snapshotManager.getAllSnapshots().size() - 1);
        store.put(id, lastSnapshot.restore());
    }
}
