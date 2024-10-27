package com.example.rutinkofffintech.task_9;

import com.example.rutinkofffintech.task_9.datastore.DataStore;
import com.example.rutinkofffintech.task_9.dto.Category;
import com.example.rutinkofffintech.task_9.controllers.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private DataStore<Category> categoryStore;

    @BeforeEach
    void setUp() {
        categoryStore = Mockito.mock(DataStore.class);
    }

    @Test
    void testGetAllCategories() throws Exception {
        Map<String, Category> categories = new HashMap<>();
        Category category = new Category();
        category.setId("1");
        category.setName("Test Category");
        categories.put("1", category);

        when(categoryStore.getAll()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCategory_Success() throws Exception {
        Category category = new Category();
        category.setId("1");
        category.setName("Test Category");

        when(categoryStore.get("1")).thenReturn(category);

        mockMvc.perform(get("/api/v1/places/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCategory_NotFound() throws Exception {
        when(categoryStore.get("2")).thenReturn(null);

        mockMvc.perform(get("/api/v1/places/categories/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCategory_Success() throws Exception {
        Category category = new Category();
        category.setId("1");
        category.setName("Test Category");

        when(categoryStore.exists(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Test Category\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCategory_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"\",\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
