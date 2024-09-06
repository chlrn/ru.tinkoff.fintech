package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.example.ObjectMapperProvider;
import org.example.XMLFileWriter;

import java.io.InputStream;

@Data
public class City {
    private final String slug;
    private final Coords coords;

    @Data
    public static class Coords {
        private final double lat;
        private final double lon;

        @JsonCreator
        public Coords(
                @JsonProperty("lat") double lat,
                @JsonProperty("lon") double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

    @JsonCreator
    public City(
            @JsonProperty("slug") String slug,
            @JsonProperty("coords") Coords coords) {
        this.slug = slug;
        this.coords = coords;
    }

    @SneakyThrows
    public static City fromJson(InputStream inputStream) {
        ObjectMapper objectMapper = ObjectMapperProvider.getInstance();
        return objectMapper.readValue(inputStream, City.class);
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
