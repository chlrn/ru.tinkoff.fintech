package com.example.rutinkofffintech.TASK_5.init;

import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

@Service
public class RateLimitedApiClient {

    private final Semaphore semaphore;

    public RateLimitedApiClient(@Value("${api.max.concurrent.requests}") int maxConcurrentRequests) {
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
