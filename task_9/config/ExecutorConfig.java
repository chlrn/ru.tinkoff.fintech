package com.example.rutinkofffintech.task_9.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ExecutorConfig {

    @Value("${thread.pool.fixed}")
    private int fixedThreadPoolSize;

    @Value("${thread.pool.scheduled}")
    private int scheduledThreadPoolSize;

    @Bean(name = "fixedThreadPoolExecutor")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(fixedThreadPoolSize,
                new ThreadFactoryBuilder()
                        .setNameFormat("FixedPool-Thread-%d")
                        .build());
    }

    @Bean(name = "scheduledThreadPoolExecutor")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize,
                new ThreadFactoryBuilder()
                        .setNameFormat("ScheduledPool-Thread-%d")
                        .build());
    }
}
