package com.example.rutinkofffintech.task_11.observer;

public interface DataStoreObserver<T> {
    void onSave(T entity);
    void onUpdate(T entity);
    void onDelete(T entity);
}
