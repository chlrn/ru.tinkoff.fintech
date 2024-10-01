package com.example.rutinkofffintech;

import com.example.rutinkofffintech.datastore.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)

public class DataStoreTest {

    private DataStore<String> dataStore;

    @BeforeEach
    public void setUp() {
        dataStore = new DataStore<>();
    }


    @Test
    public void testSaveAndGet() {
        String id = "1";
        String entity = "Test Entity";

        dataStore.save(id, entity);
        assertEquals(entity, dataStore.get(id));
    }

    @Test
    public void testUpdate() {
        String id = "1";
        String entity = "Test Entity";
        dataStore.save(id, entity);

        String updatedEntity = "Updated Entity";
        dataStore.update(id, updatedEntity);
        assertEquals(updatedEntity, dataStore.get(id));
    }

    @Test
    public void testDelete() {
        String id = "1";
        String entity = "Test Entity";
        dataStore.save(id, entity);

        dataStore.delete(id);
        assertNull(dataStore.get(id));
    }

    @Test
    public void testGetAll() {
        dataStore.save("1", "First Entity");
        dataStore.save("2", "Second Entity");

        assertEquals(2, dataStore.getAll().size());
    }

    @Test
    public void testNullIdOnSave() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.save(null, "Test Entity");
        });
        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    public void testNullEntityOnSave() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.save("1", null);
        });
        assertEquals("Entity cannot be null", exception.getMessage());
    }

    @Test
    public void testNonExistingEntityUpdate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.update("1", "Updated Entity");
        });
        assertEquals("No entity found with the given ID: 1", exception.getMessage());
    }

    @Test
    public void testNonExistingEntityDelete() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.delete("1");
        });
        assertEquals("No entity found with the given ID: 1", exception.getMessage());
    }
    @Test
    public void testGetNonExistingEntity() {
        assertNull(dataStore.get("non-existing-id"));
    }

    @Test
    public void testDeleteWithNullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.delete(null);
        });
        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    public void testUpdateWithNullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.update(null, "Updated Entity");
        });
        assertEquals("ID cannot be null", exception.getMessage());
    }


    @Test
    public void testUpdateWithNullEntity() {
        String id = "1";
        dataStore.save(id, "Test Entity");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.update(id, null);
        });
        assertEquals("Entity cannot be null", exception.getMessage());
    }


    @Test
    public void testGetAllWhenEmpty() {
        assertTrue(dataStore.getAll().isEmpty());
    }

    @Test
    public void testDeleteAll() {
        dataStore.save("1", "Entity 1");
        dataStore.save("2", "Entity 2");

        dataStore.delete("1");
        dataStore.delete("2");

        assertTrue(dataStore.getAll().isEmpty());
    }
    @Test
    public void testExists() {
        String id = "1";
        dataStore.save(id, "Test Entity");

        assertTrue(dataStore.exists(id));
        assertFalse(dataStore.exists("non-existing-id"));
    }


}
