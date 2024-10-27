package com.example.rutinkofffintech.task_11.config;

import com.example.rutinkofffintech.task_11.datastore.DataStore;
import com.example.rutinkofffintech.task_11.dto.Category;
import com.example.rutinkofffintech.task_11.dto.Location;
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
