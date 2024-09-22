package com.example.rutinkofffintech.TASK_5.config;

import com.example.rutinkofffintech.TASK_5.datastore.DataStore;
import com.example.rutinkofffintech.TASK_5.dto.Category;
import com.example.rutinkofffintech.TASK_5.dto.Location;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataStoreConfig {

    @Bean
    public DataStore<Category> categoryDataStore() {
        return new DataStore<>();
    }

    @Bean
    public DataStore<Location> locationDataStore() {
        return new DataStore<>();
    }
}
