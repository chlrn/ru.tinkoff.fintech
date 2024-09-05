package org.example;

import org.example.dto.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting JSON parsing for 'city.json'");

        // Попробуйте использовать абсолютный путь с начальным слэшем
        City city = null;
        try (InputStream cityInputStream = Main.class.getResourceAsStream("/city.json")) {
            if (cityInputStream == null) {
                logger.error("Resource 'city.json' not found");
                return;
            }
            city = City.fromJson(cityInputStream);
            if (city != null) {
                logger.info("Successfully parsed JSON for city: {}", city.getSlug());

                logger.debug("Preparing to save XML for city: {}", city.getSlug());
                city.saveToXML("city.xml");
                logger.info("XML for '{}' saved successfully", city.getSlug());
            } else {
                logger.error("Failed to parse JSON for 'city.json'");
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing 'city.json': ", e);
        }

        logger.info("Starting JSON parsing for 'city-error.json'");

        // Попробуйте использовать абсолютный путь с начальным слэшем
        City erroneousCity = null;
        try (InputStream erroneousCityInputStream = Main.class.getResourceAsStream("/city-error.json")) {
            if (erroneousCityInputStream == null) {
                logger.error("Resource 'city-error.json' not found");
                return;
            }
            erroneousCity = City.fromJson(erroneousCityInputStream);
            if (erroneousCity != null) {
                logger.info("Successfully parsed erroneous JSON for city: {}", erroneousCity.getSlug());

                logger.debug("Preparing to save XML for erroneous city: {}", erroneousCity.getSlug());
                erroneousCity.saveToXML("city-error.xml");
                logger.info("XML for erroneous city '{}' saved successfully", erroneousCity.getSlug());
            } else {
                logger.error("Failed to parse erroneous JSON for 'city-error.json'");
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing 'city-error.json': ", e);
        }

        // Пример использования WARN уровня логирования
        if (city == null || erroneousCity == null) {
            logger.warn("At least one city failed to parse. Check your JSON files for possible errors.");
        }
    }
}
