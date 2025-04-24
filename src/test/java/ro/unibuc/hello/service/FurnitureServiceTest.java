package ro.unibuc.hello.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import ro.unibuc.hello.data.FurnitureEntity;
import ro.unibuc.hello.data.FurnitureRepository;
import ro.unibuc.hello.data.SupplierRepository;
import ro.unibuc.hello.data.CategoryRepository;
import ro.unibuc.hello.dto.CreateFurniture;
import ro.unibuc.hello.dto.UpdateFurniture;
import ro.unibuc.hello.exception.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
class FurnitureServiceTest {
    @Mock
    private FurnitureRepository furnitureRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MeterRegistry meterRegistry;

    @InjectMocks
    private FurnitureService furnitureService = new FurnitureService();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFurnitureBySku() throws EntityNotFoundException {
        // Arrange
        String sku = "SKU";
        when(furnitureRepository.findBySku(sku)).thenReturn(Optional.of(new FurnitureEntity("Name", sku, 1, 10, 3, "Material", "Description", "1234")));

        // Act
        CreateFurniture furniture = furnitureService.getFurnitureBySku(sku);

        // Assert
        assertNotNull(furniture);
        assertEquals(sku, furniture.getSku());
        assertEquals("Name", furniture.getName());
        assertEquals("1234", furniture.getSupplierId());
        assertEquals("Material", furniture.getMaterial());
        assertEquals("Description", furniture.getDescription());
        assertEquals(1, furniture.getCategoryCode());
        assertEquals(10, furniture.getPrice());
        assertEquals(3, furniture.getStockQuantity());
    }

    @Test
    void testGetFurnitureBySku_NotFound() {
        // Arrange
        String sku = "SKU";
        when(furnitureRepository.findBySku(sku)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> furnitureService.getFurnitureBySku(sku));
    }

    @Test
    void testGetAllFurniture() {
        // Arrange
        when(furnitureRepository.findAll()).thenReturn(List.of(new FurnitureEntity("Name 1", "SKU 1", 1, 10, 3, "Material 1", "Description 1", "1234"),
                new FurnitureEntity("Name 2", "SKU 2", 2, 20, 5, "Material 2", "Description 2", "5678")));

        // Act
        List<CreateFurniture> furniture = furnitureService.getAllFurniture();

        // Assert
        assertNotNull(furniture);
        assertEquals(2, furniture.size());

        assertEquals("SKU 1", furniture.get(0).getSku());
        assertEquals("Name 1", furniture.get(0).getName());
        assertEquals("1234", furniture.get(0).getSupplierId());
        assertEquals("Material 1", furniture.get(0).getMaterial());
        assertEquals("Description 1", furniture.get(0).getDescription());
        assertEquals(1, furniture.get(0).getCategoryCode());
        assertEquals(10, furniture.get(0).getPrice());
        assertEquals(3, furniture.get(0).getStockQuantity());

        assertEquals("SKU 2", furniture.get(1).getSku());
        assertEquals("Name 2", furniture.get(1).getName());
        assertEquals("5678", furniture.get(1).getSupplierId());
        assertEquals("Material 2", furniture.get(1).getMaterial());
        assertEquals("Description 2", furniture.get(1).getDescription());
        assertEquals(2, furniture.get(1).getCategoryCode());
        assertEquals(20, furniture.get(1).getPrice());
        assertEquals(5, furniture.get(1).getStockQuantity());
    }


    @Test
    void testSaveFurniture() {
        // Arrange
        CreateFurniture furnitureDto = new CreateFurniture("Name", "SKU", 1, 10, 3, "Material", "Description", "1234");

        // Mock supplier and category existing check
        when(supplierRepository.existsById("1234")).thenReturn(true);
        when(categoryRepository.existsByCategoryCode(1)).thenReturn(true);

        // Mock save method
        when(furnitureRepository.save(any(FurnitureEntity.class))).thenReturn(new FurnitureEntity("Name", "SKU", 1, 10, 3, "Material", "Description", "1234"));

        // Act
        CreateFurniture furniture = furnitureService.saveFurniture(furnitureDto);

        // Assert
        assertNotNull(furniture);
        assertEquals("SKU", furniture.getSku());
        assertEquals("Name", furniture.getName());
        assertEquals("1234", furniture.getSupplierId());
        assertEquals("Material", furniture.getMaterial());
        assertEquals("Description", furniture.getDescription());
        assertEquals(1, furniture.getCategoryCode());
        assertEquals(10, furniture.getPrice());
        assertEquals(3, furniture.getStockQuantity());
    }

    @Test
    void testSaveAllFurniture() {
        // Arrange
        List<CreateFurniture> furnitureDtos = List.of(new CreateFurniture("Name 1", "SKU 1", 1, 10, 3, "Material 1", "Description 1", "1234"),
                new CreateFurniture("Name 2", "SKU 2", 2, 20, 5, "Material 2", "Description 2", "5678"));
        when(furnitureRepository.saveAll(anyList())).thenReturn(List.of(new FurnitureEntity("Name 1", "SKU 1", 1, 10, 3, "Material 1", "Description 1", "1234"),
                new FurnitureEntity("Name 2", "SKU 2", 2, 20, 5, "Material 2", "Description 2", "5678")));

        // Act
        List<CreateFurniture> furniture = furnitureService.saveAllFurniture(furnitureDtos);

        // Assert
        assertNotNull(furniture);
        assertEquals(2, furniture.size());

        assertEquals("SKU 1", furniture.get(0).getSku());
        assertEquals("Name 1", furniture.get(0).getName());
        assertEquals("1234", furniture.get(0).getSupplierId());
        assertEquals("Material 1", furniture.get(0).getMaterial());
        assertEquals("Description 1", furniture.get(0).getDescription());
        assertEquals(1, furniture.get(0).getCategoryCode());
        assertEquals(10, furniture.get(0).getPrice());
        assertEquals(3, furniture.get(0).getStockQuantity());

        assertEquals("SKU 2", furniture.get(1).getSku());
        assertEquals("Name 2", furniture.get(1).getName());
        assertEquals("5678", furniture.get(1).getSupplierId());
        assertEquals("Material 2", furniture.get(1).getMaterial());
        assertEquals("Description 2", furniture.get(1).getDescription());
        assertEquals(2, furniture.get(1).getCategoryCode());
        assertEquals(20, furniture.get(1).getPrice());
        assertEquals(5, furniture.get(1).getStockQuantity());
    }

    @Test
    void testUpdateFurniture_ExistingEntity() throws EntityNotFoundException {
        // Arrange
        String sku = "SKU";
        UpdateFurniture furnitureDto = new UpdateFurniture("Name", "SKU", 1, 10, 3, "Material", "Description", "1234");

        // Mock supplier and category existing check
        when(supplierRepository.existsById("1234")).thenReturn(true);
        when(categoryRepository.existsByCategoryCode(1)).thenReturn(true);

        // Mock fetching an existing entity
        when(furnitureRepository.findBySku(sku)).thenReturn(Optional.of(new FurnitureEntity("Name", "SKU", 1, 10, 3, "Material", "Description", "12354")));

        // Mock savin the updated entity
        when(furnitureRepository.save(any(FurnitureEntity.class))).thenReturn(new FurnitureEntity("Name", "SKU", 1, 10, 3, "Material", "Description", "1234"));

        // Act
        UpdateFurniture furniture = furnitureService.updateFurniture(sku, furnitureDto);

        // Assert
        assertNotNull(furniture);
        assertEquals("SKU", furniture.getSku());
        assertEquals("Name", furniture.getName());
        assertEquals("1234", furniture.getSupplierId());
        assertEquals("Material", furniture.getMaterial());
        assertEquals("Description", furniture.getDescription());
        assertEquals(1, furniture.getCategoryCode());
        assertEquals(10, furniture.getPrice());
        assertEquals(3, furniture.getStockQuantity());
    }

    @Test
    void testUpdateFurniture_NonExistingEntity() {
        // Arrange
        String sku = "SKU";
        UpdateFurniture furnitureDto = new UpdateFurniture("Name", "SKU", 1, 10, 3, "Material", "Description", "1234");
        when(furnitureRepository.findBySku(sku)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> furnitureService.updateFurniture(sku, furnitureDto));
    }

    @Test
    void testDeleteFurniture_ExistingEntity() throws EntityNotFoundException {
        // Arrange
        String sku = "SKU";
        FurnitureEntity entity = new FurnitureEntity("Name", sku, 1, 10, 3, "Material", "Description", "1234");
        when(furnitureRepository.findBySku(sku)).thenReturn(Optional.of(entity));

        // Act
        furnitureService.deleteFurniture(sku);

        // Assert
        verify(furnitureRepository, times(1)).delete(entity);
    }

    @Test
    void testDeleteFurniture_NonExistingEntity() {
        // Arrange
        String sku = "SKU";
        when(furnitureRepository.findBySku(sku)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> furnitureService.deleteFurniture(sku));
    }

    @Test
    void testDeleteAllFurniture() {
        // Act
        furnitureService.deleteAllFurniture();

        // Assert
        verify(furnitureRepository, times(1)).deleteAll();
    }

    @Test
    void testGetLowStockFurniture() {
        // Arrange
        int stockThreshold = 5;
        when(furnitureRepository.findAll()).thenReturn(List.of(
                new FurnitureEntity("Name 1", "SKU 1", 1, 10, 3, "Material 1", "Description 1", "1234"),
                new FurnitureEntity("Name 2", "SKU 2", 2, 20, 6, "Material 2", "Description 2", "5678"),
                new FurnitureEntity("Name 3", "SKU 3", 3, 30, 2, "Material 3", "Description 3", "91011")
        ));

        // Act
        List<CreateFurniture> lowStockFurniture = furnitureService.getLowStockFurniture(stockThreshold);

        // Assert
        assertNotNull(lowStockFurniture);

        assertEquals("SKU 1", lowStockFurniture.get(0).getSku());
        assertEquals("Name 1", lowStockFurniture.get(0).getName());
        assertEquals("1234", lowStockFurniture.get(0).getSupplierId());
        assertEquals("Material 1", lowStockFurniture.get(0).getMaterial());
        assertEquals("Description 1", lowStockFurniture.get(0).getDescription());
        assertEquals(1, lowStockFurniture.get(0).getCategoryCode());
        assertEquals(10, lowStockFurniture.get(0).getPrice());
        assertEquals(3, lowStockFurniture.get(0).getStockQuantity());

        assertEquals("SKU 3", lowStockFurniture.get(1).getSku());
        assertEquals("Name 3", lowStockFurniture.get(1).getName());
        assertEquals("91011", lowStockFurniture.get(1).getSupplierId());
        assertEquals("Material 3", lowStockFurniture.get(1).getMaterial());
        assertEquals("Description 3", lowStockFurniture.get(1).getDescription());
        assertEquals(3, lowStockFurniture.get(1).getCategoryCode());
        assertEquals(30, lowStockFurniture.get(1).getPrice());
        assertEquals(2, lowStockFurniture.get(1).getStockQuantity());
    }

    @Test
    void testGetTotalLowStockFurniture() {
        // Arrange
        int stockThreshold = 5;
        when(furnitureRepository.findAll()).thenReturn(List.of(
                new FurnitureEntity("Name 1", "SKU 1", 1, 10, 3, "Material 1", "Description 1", "1234"),
                new FurnitureEntity("Name 2", "SKU 2", 2, 20, 6, "Material 2", "Description 2", "5678"),
                new FurnitureEntity("Name 3", "SKU 3", 3, 30, 2, "Material 3", "Description 3", "91011")
        ));

        // Act
        int totalLowStockFurniture = furnitureService.getTotalLowStockFurniture(stockThreshold);

        // Assert
        assertEquals(2, totalLowStockFurniture);
    }

}