package com.example.rutinkofffintech.TASK_2.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;

@Data
public class City {
    private final String slug;
    private final Coords coords;

    @Data
    public static class Coords {
        private final double lat;
        private final double lon;
    }

    @SneakyThrows
    public static City fromJson(String filePath) {
        ObjectMapper objectMapper = ObjectMapperProvider.getInstance();
        return objectMapper.readValue(new File(filePath), City.class);
    }

    public String toXML() {
        return """
            <City>
                <slug>%s</slug>
                <coords>
                    <lat>%s</lat>
                    <lon>%s</lon>
                </coords>
            </City>
            """.formatted(slug, coords.getLat(), coords.getLon());
    }

    public void saveToXML(String filePath) {
        XMLFileWriter.saveToXML(filePath, toXML());
    }
}
