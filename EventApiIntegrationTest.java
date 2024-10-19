import com.example.rutinkofffintech.TASK_10.dto.Event;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
public class EventApiIntegrationTest {

    @JavaDispatcher.Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:13-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateEvent() {
        Event event = new Event("Concert", LocalDateTime.now(), 100);
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/events", event, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testGetEventById() {
        Event event = restTemplate.getForObject("/api/v1/events/1", Event.class);
        assertThat(event.getName()).isEqualTo("Concert");
    }
}
