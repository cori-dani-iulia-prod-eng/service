package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.unibuc.hello.data.*;
import ro.unibuc.hello.dto.CreateSupplier;
import ro.unibuc.hello.dto.UpdateSupplier;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private FurnitureRepository furnitureRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SupplierService supplierService = new SupplierService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSuppliers() {
        //Arrange
        List<SupplierEntity> supplierEntities = Arrays.asList(
                new SupplierEntity("Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania"),
                new SupplierEntity("Supplier 2", "supplier2@yahoo.com", "234-567-8901", "124 Second St, Madrid, Spain"));
        when(supplierRepository.findAll()).thenReturn(supplierEntities);

        //Act
        List<CreateSupplier> suppliers = supplierService.getAllSuppliers();

        //Assert
        assertEquals(2, suppliers.size());

        assertEquals("Supplier 1", suppliers.get(0).getName());
        assertEquals("Supplier 2", suppliers.get(1).getName());

        assertEquals("supplier1@gmail.com", suppliers.get(0).getEmail());
        assertEquals("supplier2@yahoo.com", suppliers.get(1).getEmail());

        assertEquals("123-456-7890", suppliers.get(0).getPhone());
        assertEquals("234-567-8901", suppliers.get(1).getPhone());

        assertEquals("123 Main St, Bucharest, Romania", suppliers.get(0).getAddress());
        assertEquals("124 Second St, Madrid, Spain", suppliers.get(1).getAddress());
    }

    @Test
    void getSupplierByName_ExistingEntity() throws EntityNotFoundException {
        //Arrange
        String name = "Supplier 1";
        SupplierEntity supplierEntity = new SupplierEntity("Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania");
        when(supplierRepository.findByName(name)).thenReturn(Optional.of(supplierEntity));

        //Act
        CreateSupplier supplier = supplierService.getSupplierByName(name);

        //Assert
        assertNotNull(supplier);
        assertEquals(name, supplier.getName());
        assertEquals("supplier1@gmail.com", supplier.getEmail());
        assertEquals("123-456-7890", supplier.getPhone());
        assertEquals("123 Main St, Bucharest, Romania", supplier.getAddress());
    }

    @Test
    void getSupplierByName_NonExistingEntity() {
        //Arrange
        String name = "NonExistingSupplier";
        when(supplierRepository.findByName(name)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(EntityNotFoundException.class, () -> supplierService.getSupplierByName(name));
    }

    @Test
    void saveSupplier(){
        //Arrange
        CreateSupplier createSupplier = new CreateSupplier("1", "Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania");

        //Act
        when(supplierRepository.save(any(SupplierEntity.class))).thenReturn(new SupplierEntity("1", "Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania"));
        CreateSupplier savedSupplier = supplierService.saveSupplier(createSupplier);

        //Assert
        assertNotNull(savedSupplier);
        assertEquals("Supplier 1", savedSupplier.getName());
        assertEquals("supplier1@gmail.com", savedSupplier.getEmail());
        assertEquals("123-456-7890", savedSupplier.getPhone());
        assertEquals("123 Main St, Bucharest, Romania", savedSupplier.getAddress());
    }

    @Test
    void saveAll() {
        //Arrange
        List<CreateSupplier> createSuppliers = List.of(
                new CreateSupplier("1", "Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania"),
                new CreateSupplier("2", "Supplier 2", "supplier2@yahoo.com", "234-567-8901", "124 Second St, Madrid, Spain"));
        when(supplierRepository.saveAll(anyList())).thenReturn(List.of(
                new SupplierEntity("Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania"),
                new SupplierEntity("Supplier 2", "supplier2@yahoo.com", "234-567-8901", "124 Second St, Madrid, Spain")));

        //Act
        List<CreateSupplier> savedSuppliers = supplierService.saveAll(createSuppliers);

        //Assert
        assertEquals(2, savedSuppliers.size());

        assertEquals("Supplier 1", savedSuppliers.get(0).getName());
        assertEquals("Supplier 2", savedSuppliers.get(1).getName());

        assertEquals("supplier1@gmail.com", savedSuppliers.get(0).getEmail());
        assertEquals("supplier2@yahoo.com", savedSuppliers.get(1).getEmail());

        assertEquals("123-456-7890", savedSuppliers.get(0).getPhone());
        assertEquals("234-567-8901", savedSuppliers.get(1).getPhone());

        assertEquals("123 Main St, Bucharest, Romania", savedSuppliers.get(0).getAddress());
        assertEquals("124 Second St, Madrid, Spain", savedSuppliers.get(1).getAddress());
    }

    @Test
    void updateSupplier_ExistingEntity() throws EntityNotFoundException {
        //Arrange
        String id = "1";
        UpdateSupplier updateSupplier = new UpdateSupplier("1","Updated Supplier", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania");
        SupplierEntity supplierEntity = new SupplierEntity("1","Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania");

        when(supplierRepository.findById(id)).thenReturn(Optional.of(supplierEntity));
        when(supplierRepository.save(any(SupplierEntity.class))).thenReturn(new SupplierEntity("1", "Updated Supplier", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania"));

        //Act
        UpdateSupplier updatedSupplier = supplierService.updateSupplier(id, updateSupplier);

        //Assert
        assertNotNull(updatedSupplier);
        assertEquals(id, updatedSupplier.getId());
        assertEquals("Updated Supplier", updatedSupplier.getName());
        assertEquals("supplier1@gmail.com", updatedSupplier.getEmail());
        assertEquals("123-456-7890", updatedSupplier.getPhone());
        assertEquals("123 Main St, Bucharest, Romania", updatedSupplier.getAddress());
    }

    @Test
    void updateSupplier_NonExistingEntity() {
        //Arrange
        String id = "NonExistingId";
        UpdateSupplier updateSupplier = new UpdateSupplier("1", "Updated Supplier", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania");
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(EntityNotFoundException.class, () -> supplierService.updateSupplier(id, updateSupplier));
    }

    @Test
    void deleteSupplier_ExistingEntity() throws EntityNotFoundException {
        //Arrange
        String id = "1";
        SupplierEntity supplierEntity = new SupplierEntity("Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania");
        when(supplierRepository.findById(id)).thenReturn(Optional.of(supplierEntity));

        //Act
        supplierService.deleteSupplier(id);

        //Assert
        verify(supplierRepository, times(1)).delete(supplierEntity);
    }

    @Test
    void deleteSupplier_NonExistingEntity() {
        //Arrange
        String id = "NonExistingId";
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(EntityNotFoundException.class, () -> supplierService.deleteSupplier(id));
    }

    @Test
    void deleteAllSuppliers() {
        //Act
        supplierService.deleteAllSuppliers();

        //Assert
        verify(supplierRepository, times(1)).deleteAll();
    }

    @Test
    void testGetFurnitureCountByCategoryAndSupplier() {
        // Arrange
        List<FurnitureEntity> allFurniture = List.of(
                new FurnitureEntity("Name 1", "SKU 1", 101, 10, 3, "Material 1", "Description 1", "1"),
                new FurnitureEntity("Name 2", "SKU 2", 101, 20, 6, "Material 2", "Description 2", "2"),
                new FurnitureEntity("Name 3", "SKU 3", 102, 30, 2, "Material 3", "Description 3", "1")
        );
        List<SupplierEntity> allSuppliers = List.of(
                new SupplierEntity("1", "Supplier 1", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania"),
                new SupplierEntity("2", "Supplier 2", "supplier1@gmail.com", "123-456-7890", "123 Main St, Bucharest, Romania")
        );
        List<CategoryEntity> allCategories = List.of(
                new CategoryEntity(101, "Category 1"),
                new CategoryEntity(102, "Category 2")
        );

        when(furnitureRepository.findAll()).thenReturn(allFurniture);
        when(supplierRepository.findAll()).thenReturn(allSuppliers);
        when(categoryRepository.findAll()).thenReturn(allCategories);

        // Act
        Map<String, Map<String, Long>> result = supplierService.getFurnitureCountByCategoryAndSupplier();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("Supplier 1"));
        assertTrue(result.containsKey("Supplier 2"));

        Map<String, Long> supplier1Categories = result.get("Supplier 1");
        assertEquals(2, supplier1Categories.size());
        assertEquals(1L, supplier1Categories.get("Category 1"));
        assertEquals(1L, supplier1Categories.get("Category 2"));

        Map<String, Long> supplier2Categories = result.get("Supplier 2");
        assertEquals(1, supplier2Categories.size());
        assertEquals(1L, supplier2Categories.get("Category 1"));
    }
}