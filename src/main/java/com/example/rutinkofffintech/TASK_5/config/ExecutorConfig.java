package com.example.rutinkofffintech.TASK_5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExecutorConfig {

    @Bean("fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        int threadCount = 5; // кол-во потоков должно браться из конфигурации
        return Executors.newFixedThreadPool(threadCount, new CustomThreadFactory("FixedPool-Thread"));
    }

    @Bean("scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(2, new CustomThreadFactory("ScheduledPool-Thread"));
    }

    private static class CustomThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private int count = 0;

        public CustomThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + "-" + count++);
        }
    }
}

