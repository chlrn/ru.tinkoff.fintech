package com.example.rutinkofffintech.TASK_5;

import com.example.rutinkofffintech.TASK_5.datastore.DataStore;
import com.example.rutinkofffintech.TASK_5.dto.Category;
import com.example.rutinkofffintech.TASK_5.dto.Location;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public DataStore<Category> categoryDataStore() {
        return new DataStore<>();
    }

    @Bean
    public DataStore<Location> locationDataStore() {
        return new DataStore<>();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

