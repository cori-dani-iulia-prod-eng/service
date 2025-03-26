package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.unibuc.hello.data.FurnitureRepository;
import ro.unibuc.hello.data.StockMovementEntity;
import ro.unibuc.hello.data.StockMovementRepository;
import ro.unibuc.hello.dto.CreateStockMovement;
import ro.unibuc.hello.dto.UpdateStockMovement;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class StockMovementServiceTest {

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private FurnitureRepository furnitureRepository;

    @InjectMocks
    private StockMovementService stockMovementService= new StockMovementService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStockMovements() {
        Date date = new Date();
        when(stockMovementRepository.findAll()).thenReturn(List.of(new StockMovementEntity("1", "1", 1, date),
                new StockMovementEntity("2", "2", 2, date)));

        List<CreateStockMovement> stockMovements = stockMovementService.getAllStockMovements();

        assertEquals(2, stockMovements.size());
        assertEquals("1", stockMovements.get(0).getFurnitureId());
        assertEquals(1, stockMovements.get(0).getQuantity());
        assertEquals("2", stockMovements.get(1).getFurnitureId());
        assertEquals(2, stockMovements.get(1).getQuantity());
        assertEquals(date, stockMovements.get(0).getTimestamp());
        assertEquals(date, stockMovements.get(1).getTimestamp());
    }

    @Test
    void getStockMovementById() throws EntityNotFoundException {
        Date date = new Date();
        String id = "1";
        when(stockMovementRepository.findById(id)).thenReturn(java.util.Optional.of(new StockMovementEntity("1", "1", 1, date)));

        CreateStockMovement stockMovement = stockMovementService.getStockMovementById("1");

        assertNotNull(stockMovement);
        assertEquals("1", stockMovement.getFurnitureId());
        assertEquals(1, stockMovement.getQuantity());
        assertEquals(date, stockMovement.getTimestamp());
    }

    @Test
    void getStockMovementByIdNotFound() {
        String id = "1";
        when(stockMovementRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> stockMovementService.getStockMovementById(id));
    }

    @Test
    void saveStockMovement() {
        Date date = new Date();
        CreateStockMovement createStockMovement = new CreateStockMovement("1", 1, date);
        when(furnitureRepository.existsById("1")).thenReturn(true);

        when(stockMovementRepository.save(any(StockMovementEntity.class))).thenReturn(new StockMovementEntity("1", "1", 1, date));

        CreateStockMovement savedStockMovement = stockMovementService.saveStockMovement(createStockMovement);

        assertNotNull(savedStockMovement);
        assertEquals("1", savedStockMovement.getFurnitureId());
        assertEquals(1, savedStockMovement.getQuantity());
        assertEquals(date, savedStockMovement.getTimestamp());
    }

    @Test
    void saveAll() {
        Date date = new Date();
        List<CreateStockMovement> createStockMovements = List.of(new CreateStockMovement("1", 1, date),
                new CreateStockMovement("2", 2, date));
        when(furnitureRepository.existsById("1")).thenReturn(true);
        when(furnitureRepository.existsById("2")).thenReturn(true);
        when(stockMovementRepository.saveAll(anyList())).thenReturn(List.of(new StockMovementEntity("1", "1", 1, date),
                new StockMovementEntity("2", "2", 2, date)));

        List<CreateStockMovement> savedStockMovements = stockMovementService.saveAll(createStockMovements);

        assertEquals(2, savedStockMovements.size());
        assertEquals("1", savedStockMovements.get(0).getFurnitureId());
        assertEquals(1, savedStockMovements.get(0).getQuantity());
        assertEquals("2", savedStockMovements.get(1).getFurnitureId());
        assertEquals(2, savedStockMovements.get(1).getQuantity());
        assertEquals(date, savedStockMovements.get(0).getTimestamp());
        assertEquals(date, savedStockMovements.get(1).getTimestamp());
    }

    @Test
    void updateStockMovement() throws EntityNotFoundException {
        Date date = new Date();
        String id = "1";
        UpdateStockMovement updateStockMovement = new UpdateStockMovement("1", 1, date);

        when(furnitureRepository.existsById(id)).thenReturn(true);

        when(stockMovementRepository.findById(id)).thenReturn(java.util.Optional.of(new StockMovementEntity("1", "1", 12, date)));

        when(stockMovementRepository.save(any(StockMovementEntity.class))).thenReturn(new StockMovementEntity("1", "1", 1, date));

        UpdateStockMovement updatedStockMovement = stockMovementService.updateStockMovement(id, updateStockMovement);

        assertNotNull(updatedStockMovement);
        assertEquals("1", updatedStockMovement.getFurnitureId());
        assertEquals(1, updatedStockMovement.getQuantity());
        assertEquals(date, updatedStockMovement.getTimestamp());
    }

    @Test
    void updateStockMovementNotFound() {
        String id = "1";
        Date date = new Date();
        UpdateStockMovement updateStockMovement = new UpdateStockMovement("1", 1, date);
        when(stockMovementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> stockMovementService.updateStockMovement(id, updateStockMovement));
    }

    @Test
    void deleteStockMovement() {
        String id = "1";
        StockMovementEntity entity = new StockMovementEntity("1", "1", 1, new Date());
        when(stockMovementRepository.findById(id)).thenReturn(java.util.Optional.of(entity));

        stockMovementService.deleteStockMovement(id);

        verify(stockMovementRepository, times(1)).delete(entity);
    }

    @Test
    void deleteStockMovementNotFound() throws EntityNotFoundException {
        String id = "1";
        when(stockMovementRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> stockMovementService.deleteStockMovement(id));
    }

    @Test
    void deleteAllStockMovements() {
        stockMovementService.deleteAllStockMovements();

        verify(stockMovementRepository, times(1)).deleteAll();
    }
}