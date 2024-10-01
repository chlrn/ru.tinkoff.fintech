package com.example.rutinkofffintech;

import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataStore<Category> categoryStore;

    private final String CATEGORY_ID = "1";
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(CATEGORY_ID);
        testCategory.setName("Test Category");
    }

    @Test
    public void getAllCategoriesTest() throws Exception {
        when(categoryStore.getAll()).thenReturn(Map.of(CATEGORY_ID, testCategory));

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + CATEGORY_ID + ".name").value("Test Category")); // Используем доступ по ключу
    }



    @Test
    public void getCategoryByIdTest() throws Exception {
        when(categoryStore.get(CATEGORY_ID)).thenReturn(testCategory);

        mockMvc.perform(get("/api/v1/places/categories/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    public void createCategoryTest() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Test Category\"}"))
                .andExpect(status().isCreated()); // Изменено на 201 Created

        verify(categoryStore, times(1)).save(eq(CATEGORY_ID), any(Category.class));
    }


    @Test
    public void updateCategoryTest() throws Exception {
        when(categoryStore.exists(CATEGORY_ID)).thenReturn(true); // Проверка существования
        when(categoryStore.get(CATEGORY_ID)).thenReturn(testCategory); // Возвращаем тестовую категорию

        mockMvc.perform(put("/api/v1/places/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + CATEGORY_ID + "\",\"name\":\"Updated Category\"}"))
                .andExpect(status().isOk());

        verify(categoryStore, times(1)).update(eq(CATEGORY_ID), any(Category.class));
    }



    @Test
    public void deleteCategoryTest() throws Exception {
        when(categoryStore.exists(CATEGORY_ID)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/places/categories/{id}", CATEGORY_ID))
                .andExpect(status().isNoContent()); // 204 No Content

        verify(categoryStore, times(1)).delete(CATEGORY_ID);
    }

    // Дополнительные тесты
    @Test
    public void createCategoryWithNullIdTest() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"Invalid Category\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createCategoryWithEmptyIdTest() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"\",\"name\":\"Valid Name\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createCategoryWithEmptyRequestBodyTest() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Пустой объект
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoryNotFoundTest() throws Exception {
        when(categoryStore.get(CATEGORY_ID)).thenReturn(null);

        mockMvc.perform(get("/api/v1/places/categories/{id}", CATEGORY_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateNonExistingCategoryTest() throws Exception {
        when(categoryStore.get(CATEGORY_ID)).thenReturn(null);

        mockMvc.perform(put("/api/v1/places/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Non-existing Category\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteNonExistingCategoryTest() throws Exception {
        when(categoryStore.get(CATEGORY_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/places/categories/{id}", CATEGORY_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCategoryWithNullIdTest() throws Exception {
        mockMvc.perform(put("/api/v1/places/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"Updated Category\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCategoryWithNullCategoryTest() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCategoryWithEmptyNameTest() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCategoryWithInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/api/v1/places/categories/{id}", ""))
                .andExpect(status().isBadRequest()); // Ожидаем статус 400
    }


    @Test
    public void createCategoryWithExistingIdTest() throws Exception {
        when(categoryStore.exists(CATEGORY_ID)).thenReturn(true); // Мокаем проверку на существование

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Test Category\"}"))
                .andExpect(status().isConflict()); // Ожидаем статус 409 Conflict
    }


    @Test
    public void updateCategoryWithNonExistentIdTest() throws Exception {
        mockMvc.perform(put("/api/v1/places/categories/{id}", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"999\",\"name\":\"Non-existent Category\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCategoryByInvalidIdTest() throws Exception {
        mockMvc.perform(get("/api/v1/places/categories/{id}", ""))
                .andExpect(status().isBadRequest()); // Проверка на пустой ID
    }


    @Test
    public void getAllCategoriesEmptyTest() throws Exception {
        when(categoryStore.getAll()).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getCategoryThrowsExceptionTest() throws Exception {
        when(categoryStore.get(anyString())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/places/categories/{id}", CATEGORY_ID))
                .andExpect(status().isInternalServerError());
    }

}
