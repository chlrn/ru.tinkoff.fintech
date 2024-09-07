package com.example.rutinkofffintech.TASK_2.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting JSON parsing");

        // Чтение города из файла JSON
        City city = City.fromJson("city.json");
        if (city != null) {
            logger.info("Successfully parsed JSON");

            // Сохранение в файл XML
            city.saveToXML("city.xml");
            logger.info("XML saved successfully");
        } else {
            logger.error("Failed to parse JSON");
        }

        logger.info("Starting JSON parsing for city-error.json");

        // Чтение города с ошибкой из другого файла JSON
        City erroneousCity = City.fromJson("city-error.json");
        if (erroneousCity != null) {
            logger.info("Successfully parsed erroneous JSON");

            // Сохранение ошибочного города в файл XML
            erroneousCity.saveToXML("city-error.xml");
            logger.info("Erroneous XML saved successfully");
        } else {
            logger.error("Failed to parse erroneous JSON");
        }
    }
}
