package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.CreateStockMovement;
import ro.unibuc.hello.dto.UpdateStockMovement;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.filters.GlobalExceptionFilter;
import ro.unibuc.hello.service.StockMovementService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StockMovementControllerTest {

    @Mock
    private StockMovementService stockMovementService;

    @InjectMocks
    private StockMovementController stockMovementController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stockMovementController).setControllerAdvice(new GlobalExceptionFilter()).build();
    }

    @Test
    void test_getAllStockMovements() throws Exception {
        // Arrange
        Date date = new Date();
        List<CreateStockMovement> stockMovements = Arrays.asList(
                new CreateStockMovement("1", 1, date),
                new CreateStockMovement("2", 2, date)
        );
        when(stockMovementService.getAllStockMovements()).thenReturn(stockMovements);

        // Act & Assert
        mockMvc.perform(get("/stock_movements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].furnitureId").value("1"))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[0].timestamp").value(date))
                .andExpect(jsonPath("$[1].furnitureId").value("2"))
                .andExpect(jsonPath("$[1].quantity").value(2))
                .andExpect(jsonPath("$[1].timestamp").value(date));
    }

    @Test
    void getStockMovementById() throws Exception {
        Date date = new Date();
        String id = "1";
        CreateStockMovement stockMovement = new CreateStockMovement("1", 1, date);
        when(stockMovementService.getStockMovementById(id)).thenReturn(stockMovement);

        mockMvc.perform(get("/stock_movements/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.furnitureId").value(id))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.timestamp").value(date));
    }

    @Test
    void getStockMovementById_notFound() throws Exception {
        String id = "1";
        when(stockMovementService.getStockMovementById(id)).thenThrow(new EntityNotFoundException(id));

        mockMvc.perform(get("/stock_movements/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: "+id+" was not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createStockMovement() throws Exception {
        Date date = new Date();
        CreateStockMovement stockMovement = new CreateStockMovement("1", 1, date);

        when(stockMovementService.saveStockMovement(any(CreateStockMovement.class))).thenReturn(stockMovement);

        mockMvc.perform(post("/stock_movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"furnitureId\":\"1\",\"quantity\":1,\"timestamp\":\"" + date.getTime() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.furnitureId").value("1"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.timestamp").value(date.getTime()));
    }

    @Test
    void createStockMovement_InvalidInput() throws Exception {
        // Arrange
        String invalidStockMovementJson = "{\"id\":\"1\",\"quantity\":1}";
        doThrow(new InvalidInputException("Invalid data")).when(stockMovementService).saveStockMovement(any(CreateStockMovement.class));

        // Act & Assert
        mockMvc.perform(post("/stock_movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidStockMovementJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void saveAllStockMovements() throws Exception {
        // Arrange
        Date date = new Date();
        List<CreateStockMovement> stockMovements = Arrays.asList(
                new CreateStockMovement("1", 1, date),
                new CreateStockMovement("2", 2, date)
        );

        when(stockMovementService.saveAll(anyList())).thenReturn(stockMovements);

        // Act and Assert
        mockMvc.perform(post("/stock_movements/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"furnitureId\":\"1\",\"quantity\":1,\"timestamp\":\"" + date.getTime() + "\"}," +
                                "{\"furnitureId\":\"2\",\"quantity\":2,\"timestamp\":\"" + date.getTime() + "\"}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].furnitureId").value("1"))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[0].timestamp").value(date.getTime()))
                .andExpect(jsonPath("$[1].furnitureId").value("2"))
                .andExpect(jsonPath("$[1].quantity").value(2))
                .andExpect(jsonPath("$[1].timestamp").value(date.getTime()));
    }

    @Test
    void updateStockMovement() throws Exception {
        // Arrange
        Date date = new Date();
        String id = "1";
        UpdateStockMovement updateStockMovement = new UpdateStockMovement(id, 10, date);
        when(stockMovementService.updateStockMovement(eq(id), any(UpdateStockMovement.class))).thenReturn(updateStockMovement);

        // Act and Assert
        mockMvc.perform(put("/stock_movements/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"quantity\":1,\"date\":\"" + date + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.furnitureId").value("1"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.timestamp").value(date));
    }

    @Test
    void updateStockMovement_EntityNotFound() throws Exception {
        // Arrange
        String id = "1";
        Date date = new Date();
        UpdateStockMovement updateStockMovement = new UpdateStockMovement(id, 10, date);
        when(stockMovementService.updateStockMovement(eq(id), any(UpdateStockMovement.class))).thenThrow(new EntityNotFoundException(id));

        // Act and Assert
        mockMvc.perform(put("/stock_movements/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"quantity\":1,\"date\":\"" + date + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: " + id + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(stockMovementService, times(1)).updateStockMovement(eq(id), any(UpdateStockMovement.class));
    }

    @Test
    void updateStockMovement_InvalidInput() throws Exception {
        // Arrange
        String id = "1";
        Date date = new Date();
        String invalidStockMovementJson = "{\"id\":\"1\",\"quantity\":0,\"date\":\"" + date + "\"}";

        // Act & Assert
        mockMvc.perform(put("/stock_movements/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidStockMovementJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteStockMovement() throws Exception {
        // Arrange
        String id = "1";
        doNothing().when(stockMovementService).deleteStockMovement(eq(id));

        // Act & Assert
        mockMvc.perform(delete("/stock_movements/{id}", id))
                .andExpect(status().isNoContent());

        verify(stockMovementService, times(1)).deleteStockMovement(eq(id));
    }

    @Test
    void deleteStockMovement_EntityNotFound() throws Exception {
        // Arrange
        String id = "1";
        doThrow(new EntityNotFoundException(id)).when(stockMovementService).deleteStockMovement(id);

        // Act & Assert
        mockMvc.perform(delete("/stock_movements/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: " + id + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(stockMovementService, times(1)).deleteStockMovement(eq(id));
    }

    @Test
    void deleteAllStockMovements() throws Exception {
        // Arrange
        doNothing().when(stockMovementService).deleteAllStockMovements();

        // Act & Assert
        mockMvc.perform(delete("/stock_movements"))
                .andExpect(status().isNoContent());

        verify(stockMovementService, times(1)).deleteAllStockMovements();
    }
}