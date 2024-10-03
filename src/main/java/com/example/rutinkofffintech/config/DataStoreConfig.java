package com.example.rutinkofffintech.config;

import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Category;
import com.example.rutinkofffintech.dto.Location;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
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
