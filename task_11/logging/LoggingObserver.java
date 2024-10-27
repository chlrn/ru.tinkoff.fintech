package com.example.rutinkofffintech.task_11.logging;

import com.example.rutinkofffintech.task_11.observer.DataStoreObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingObserver<T> implements DataStoreObserver<T> {
    private static final Logger logger = LoggerFactory.getLogger(LoggingObserver.class);

    @Override
    public void onSave(T entity) {
        logger.info("Entity saved: {}", entity);
    }

    @Override
    public void onUpdate(T entity) {
        logger.info("Entity updated: {}", entity);
    }

    @Override
    public void onDelete(T entity) {
        logger.info("Entity deleted: {}", entity);
    }
}
