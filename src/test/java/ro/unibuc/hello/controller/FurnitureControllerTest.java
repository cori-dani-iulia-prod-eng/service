package ro.unibuc.hello.controller;

import io.cucumber.java.ja.但し;
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
import org.w3c.dom.ls.LSInput;
import ro.unibuc.hello.data.CategoryRepository;
import ro.unibuc.hello.data.SupplierRepository;
import ro.unibuc.hello.dto.CreateFurniture;
import ro.unibuc.hello.dto.UpdateFurniture;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.filters.GlobalExceptionFilter;
import ro.unibuc.hello.service.FurnitureService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class FurnitureControllerTest {

    @Mock
    private FurnitureService furnitureService;

    @InjectMocks
    private FurnitureController furnitureController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(furnitureController).setControllerAdvice(new GlobalExceptionFilter()).build();
    }

    @Test
    void test_getFurnitureBySku() throws Exception {
        // Arrange
        CreateFurniture furniture = new CreateFurniture("Furniture 1", "FURNITURE-1", 1, 10, 5, "Material 1", "Description 1", "1234");
        when(furnitureService.getFurnitureBySku("FURNITURE-1")).thenReturn(furniture);

        // Act & Assert
        mockMvc.perform(get("/furniture/FURNITURE-1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.sku").value("FURNITURE-1"))
               .andExpect(jsonPath("$.name").value("Furniture 1"))
               .andExpect(jsonPath("$.description").value("Description 1"))
               .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.stockQuantity").value(5))
        .andExpect(jsonPath("$.material").value("Material 1"))
        .andExpect(jsonPath("$.categoryCode").value(1))
        .andExpect(jsonPath("$.supplierId").value("1234"));

    }

    @Test
    void test_getFurnitureBySku_EntityNotFound() throws Exception {
        // Arrange
        String sku = "FURNITURE-1";
        when(furnitureService.getFurnitureBySku(sku)).thenThrow(new EntityNotFoundException("Furniture with SKU " + sku));

        // Act & Assert
        mockMvc.perform(get("/furniture/{sku}", sku))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: Furniture with SKU " + sku + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void test_getAllFurniture() throws Exception {
        // Arrange
        List<CreateFurniture> furniture = Arrays.asList(new CreateFurniture("Furniture 1", "FURNITURE-1", 1, 10, 5, "Material 1", "Description 1", "1234"),
                new CreateFurniture("Furniture 2", "FURNITURE-2", 2, 20, 10, "Material 2", "Description 2", "5678"));
        when(furnitureService.getAllFurniture()).thenReturn(furniture);

        // Act & Assert
        mockMvc.perform(get("/furniture"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("FURNITURE-1"))
                .andExpect(jsonPath("$[0].name").value("Furniture 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].price").value(10))
                .andExpect(jsonPath("$[0].stockQuantity").value(5))
                .andExpect(jsonPath("$[0].material").value("Material 1"))
                .andExpect(jsonPath("$[0].categoryCode").value(1))
                .andExpect(jsonPath("$[0].supplierId").value("1234"))
                .andExpect(jsonPath("$[1].sku").value("FURNITURE-2"))
                .andExpect(jsonPath("$[1].name").value("Furniture 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].price").value(20))
                .andExpect(jsonPath("$[1].stockQuantity").value(10))
                .andExpect(jsonPath("$[1].material").value("Material 2"))
                .andExpect(jsonPath("$[1].categoryCode").value(2))
                .andExpect(jsonPath("$[1].supplierId").value("5678"));
    }

    @Test
    void test_createFurniture() throws Exception {
        // Arrange
        CreateFurniture furniture = new CreateFurniture("Furniture 1", "FURNITURE-001", 1, 10, 5, "Material 1", "Description 1", "1234");

        when(furnitureService.saveFurniture(any(CreateFurniture.class))).thenReturn(furniture);

        // Act & Assert
        mockMvc.perform(post("/furniture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Furniture 1\",\"sku\":\"FURNITURE-001\",\"categoryCode\":1,\"price\":10,\"stockQuantity\":5,\"material\":\"Material 1\",\"description\":\"Description 1\",\"supplierId\":\"1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("FURNITURE-001"))
                .andExpect(jsonPath("$.name").value("Furniture 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.stockQuantity").value(5))
                .andExpect(jsonPath("$.material").value("Material 1"))
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.supplierId").value("1234"));
    }

    @Test
    void test_createFurniture_InvalidInput() throws Exception {
        // Arrange
        String invalidFurnitureJson = "{\"name\":\"Furniture 1\",\"sku\":\"FURNITURE-1\",\"categoryCode\":1,\"price\":10,\"stockQuantity\":5,\"material\":\"Material 1\",\"description\":\"Description 1\",\"supplierId\":\"1234\"}";
        doThrow(new InvalidInputException("Invalid data")).when(furnitureService).saveFurniture(any(CreateFurniture.class));

        // Act & Assert
        mockMvc.perform(post("/furniture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFurnitureJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

    }

    @Test
    void test_updateFurniture() throws Exception {
        // Arrange
        String sku = "FURNITURE-1";
        UpdateFurniture furniture = new UpdateFurniture("Furniture 1", sku,1, 10, 5, "Material 1", "Description 1", "1234");
        when(furnitureService.updateFurniture(eq(sku), any(UpdateFurniture.class))).thenReturn(furniture);

        // Act & Assert
        mockMvc.perform(put("/furniture/{sku}", sku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Furniture 1\",\"categoryCode\":1,\"price\":10,\"stockQuantity\":5,\"material\":\"Material 1\",\"description\":\"Description 1\",\"supplierId\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Furniture 1"))
                .andExpect(jsonPath("$.sku").value("FURNITURE-1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.stockQuantity").value(5))
                .andExpect(jsonPath("$.material").value("Material 1"))
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.supplierId").value("1234"));
    }

    @Test
    void test_updateFurniture_EntityNotFound() throws Exception {
        // Arrange
        String sku = "FURNITURE-001";
        UpdateFurniture furniturDto = new UpdateFurniture("Updated Furniture 1", sku,1, 10, 5, "Material 1", "Description 1", "1234");

        when(furnitureService.updateFurniture(eq(sku), any(UpdateFurniture.class))).thenThrow(new EntityNotFoundException("Furniture with SKU " + sku));

        // Act & Assert
        mockMvc.perform(put("/furniture/{sku}", sku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Furniture 1\",\"sku\":\"FURNITURE-001\",\"categoryCode\":1,\"price\":10,\"stockQuantity\":5,\"material\":\"Material 1\",\"description\":\"Description 1\",\"supplierId\":\"1234\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: Furniture with SKU " + sku + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(furnitureService, times(1)).updateFurniture(eq(sku), any(UpdateFurniture.class));
    }

    @Test
    void test_updateFurniture_InvalidInput() throws Exception {
        // Arrange
        String sku = "FURNITURE-1";
        String invalidFurnitureJson = "{\"name\":\"Furniture 1\",\"sku\":\"FURNITURE-1\",\"categoryCode\":1,\"price\":10,\"stockQuantity\":5,\"material\":\"Material 1\",\"description\":\"Description 1\",\"supplierId\":\"1234\"}";

        // Act & Assert
        mockMvc.perform(put("/furniture/{sku}", sku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFurnitureJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void test_deleteFurniture() throws Exception {
        // Arrange
        String sku = "FURNITURE-1";
        doNothing().when(furnitureService).deleteFurniture(sku);

        // Act & Assert
        mockMvc.perform(delete("/furniture/{sku}", sku))
                .andExpect(status().isNoContent());

        verify(furnitureService, times(1)).deleteFurniture(sku);
    }

    @Test
    void test_deleteFurniture_EntityNotFound() throws Exception {
        // Arrange
        String sku = "FURNITURE-1";
        doThrow(new EntityNotFoundException("Furniture with SKU " + sku)).when(furnitureService).deleteFurniture(sku);

        // Act & Assert
        mockMvc.perform(delete("/furniture/{sku}", sku))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: Furniture with SKU " + sku + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(furnitureService, times(1)).deleteFurniture(sku);
    }

    @Test
    void test_deleteAllFurniture() throws Exception {
        // Arrange
        doNothing().when(furnitureService).deleteAllFurniture();

        // Act & Assert
        mockMvc.perform(delete("/furniture"))
                .andExpect(status().isNoContent());

        verify(furnitureService, times(1)).deleteAllFurniture();
    }

    @Test
    void test_getLowStockFurniture() throws Exception {
        // Arrange: Mocking service to return 2 furniture items
        List<CreateFurniture> lowStockFurniture = Arrays.asList(
                new CreateFurniture("Chair", "CHAIR-123", 1, 50, 2, "Wood", "A simple chair", "SUPP-1"),
                new CreateFurniture("Table", "TABLE-456", 2, 100, 3, "Metal", "A durable table", "SUPP-2")
        );

        when(furnitureService.getTotalLowStockFurniture(5)).thenReturn(2); // At least one item exists
        when(furnitureService.getLowStockFurniture(5)).thenReturn(lowStockFurniture);

        // Act & Assert
        mockMvc.perform(get("/furniture/low-stock?stockThreshold=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))  // Check 2 items in response
                .andExpect(jsonPath("$[0].name").value("Chair"))
                .andExpect(jsonPath("$[1].name").value("Table"));
    }

    @Test
    void test_getLowStockFurniture_EntityNotFound() throws Exception {
        // Arrange: Mocking service to return 0 furniture items
        when(furnitureService.getTotalLowStockFurniture(5)).thenReturn(0); // No items exist

        // Act & Assert
        mockMvc.perform(get("/furniture/low-stock?stockThreshold=5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: No low stock furniture was not found"));
    }

    /*@Test
    void test_getLowStockFurniture_InvalidStockThreshold() throws Exception {
        // Act & Assert: Sending an invalid stockThreshold (less than 1)
        mockMvc.perform(get("/furniture/low-stock?stockThreshold=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }*/


}