package ro.unibuc.hello.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.CreateCategory;
import ro.unibuc.hello.dto.UpdateCategory;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.filters.GlobalExceptionFilter;
import ro.unibuc.hello.service.CategoryService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setControllerAdvice(new GlobalExceptionFilter()).build();
    }

    @Test
    void test_getCategoryByCode() throws Exception {
        // Arrange
        CreateCategory category = new CreateCategory(1, "Category 1");
        when(categoryService.getCategoryByCode(1)).thenReturn(category);

        // Act & Assert
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    void test_getCategoryByCode_EntityNotFound() throws Exception {
        // Arrange
        int categoryCode = 1;
        when(categoryService.getCategoryByCode(categoryCode)).thenThrow(new EntityNotFoundException("Category with  " + categoryCode));

        // Act & Assert
        mockMvc.perform(get("/categories/{code}", categoryCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: Category with  " + categoryCode + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(categoryService, times(1)).getCategoryByCode(1);
    }

    @Test
    void test_getAllCategories() throws Exception {
        // Arrange
        List<CreateCategory> categories = Arrays.asList(new CreateCategory(1, "Category 1"), new CreateCategory(2, "Category 2"));
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryCode").value(1))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].categoryCode").value(2))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    @Test
    void test_createCategory() throws Exception {
        // Arrange
        CreateCategory category = new CreateCategory(1, "Category 1");
        when(categoryService.saveCategory(any(CreateCategory.class))).thenReturn(category);

        // Act & Assert
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryCode\":1,\"name\":\"Category 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    void test_createCategory_InvalidInput() throws Exception {
        // Arrange
        String invalidCategoryJson = "{\"categoryCode\":1,\"name\":\"\"}";  // Name is empty (invalid)
        doThrow(new InvalidInputException("Invalid data")).when(categoryService).saveCategory(any(CreateCategory.class));


        // Act & Assert
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCategoryJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void test_updateCategory() throws Exception {
        // Arrange
        UpdateCategory category = new UpdateCategory(1, "Category 1");
        when(categoryService.updateCategory(eq(1), any(UpdateCategory.class))).thenReturn(category);

        // Act & Assert
        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryCode\":1,\"name\":\"Category 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    void test_updateCategory_EntityNotFound() throws Exception {
        // Arrange
        int categoryCode = 99;
        UpdateCategory categoryDto = new UpdateCategory(categoryCode, "Updated Category");

        when(categoryService.updateCategory(eq(categoryCode), any(UpdateCategory.class)))
                .thenThrow(new EntityNotFoundException("Category with  " + categoryCode));

        // Act & Assert
        mockMvc.perform(put("/categories/{code}", categoryCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryCode\":99,\"name\":\"Updated Category\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: Category with  " + categoryCode + " was not found"));

        verify(categoryService, times(1)).updateCategory(eq(categoryCode), any(UpdateCategory.class));
    }


    @Test
    void test_UpdateCategory_InvalidInput() throws Exception {
        // Arrange
        int categoryCode = 99;
        String invalidCategoryJson = "{\"categoryCode\":1,\"name\":\"\"}";

        // Act & Assert
        mockMvc.perform(put("/categories/{code}", categoryCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCategoryJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

    }

    @Test
    void test_deleteCategory() throws Exception {
        int code = 1;
        CreateCategory category = new CreateCategory(code, "Category 1");
        when(categoryService.saveCategory(any(CreateCategory.class))).thenReturn(category);

        // Add a category
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryCode\":1,\"name\":\"Category 1\"}"))
                .andExpect(status().isCreated());

        // Delete the category
        mockMvc.perform(delete("/categories/{code}", code))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(code);

        // Verify that the category was deleted
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void test_deleteCategory_EntityNotFound() throws Exception {
        // Arrange
        int categoryCode = 99;
        doThrow(new EntityNotFoundException("Category with  " + categoryCode)).when(categoryService).deleteCategory(categoryCode);

        // Act & Assert
        mockMvc.perform(delete("/categories/{code}", categoryCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: Category with  " + categoryCode + " was not found"));

        verify(categoryService, times(1)).deleteCategory(categoryCode);
    }

    @Test
    void test_deleteAllCategories() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/categories"))
                .andExpect(status().isNoContent());

        // Verify that the service method was called once
        verify(categoryService, times(1)).deleteAllCategories();
    }
}