package com.example.rutinkofffintech.task_9.init;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

public class RateLimitingService {

    private final Semaphore semaphore;

    public RateLimitingService(int maxConcurrentRequests) {
        this.semaphore = new Semaphore(maxConcurrentRequests);
    }

    public <T> T executeWithRateLimiting(Callable<T> task) throws Exception {
        semaphore.acquire();
        try {
            return task.call();
        } finally {
            semaphore.release();
        }
    }
}
