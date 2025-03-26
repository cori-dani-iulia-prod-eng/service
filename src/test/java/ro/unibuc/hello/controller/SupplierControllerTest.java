package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.CreateSupplier;
import ro.unibuc.hello.dto.UpdateSupplier;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.filters.GlobalExceptionFilter;
import ro.unibuc.hello.service.SupplierService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController).setControllerAdvice(new GlobalExceptionFilter()).build();
    }

    @Test
    void getAllSuppliers() throws Exception {
        // Arrange
        List<CreateSupplier> suppliers = Arrays.asList(
                new CreateSupplier("1", "Supplier 1", "Email 1", "Phone 1", "Address 1"),
                new CreateSupplier("2", "Supplier 2", "Email 2", "Phone 2", "Address 2"));

        when(supplierService.getAllSuppliers()).thenReturn(suppliers);

        // Act
        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Supplier 1"))
                .andExpect(jsonPath("$[0].email").value("Email 1"))
                .andExpect(jsonPath("$[0].phone").value("Phone 1"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Supplier 2"))
                .andExpect(jsonPath("$[1].email").value("Email 2"))
                .andExpect(jsonPath("$[1].phone").value("Phone 2"))
                .andExpect(jsonPath("$[1].address").value("Address 2"));

    }

    @Test
    void getSupplierByName() throws Exception {
        //Arrange
        String name = "Supplier 1";
        CreateSupplier supplier = new CreateSupplier("1", name, "Email 1", "Phone 1", "Address 1");
        when(supplierService.getSupplierByName(name)).thenReturn(supplier);

        //Act and Assert
        mockMvc.perform(get("/suppliers/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Supplier 1"))
                .andExpect(jsonPath("$.email").value("Email 1"))
                .andExpect(jsonPath("$.phone").value("Phone 1"))
                .andExpect(jsonPath("$.address").value("Address 1"));
    }

    @Test
    void getSupplierByName_EntityNotFound() throws Exception {
        //Arrange
        String name = "Supplier 1";
        when(supplierService.getSupplierByName(name)).thenThrow(new EntityNotFoundException(name));

        //Act and Assert
        mockMvc.perform(get("/suppliers/{name}", name))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: " + name + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createSupplier() throws Exception {
        // Arrange
        CreateSupplier supplier = new CreateSupplier("1", "Supplier 1", "email1@gmail.com", "123-456-7890", "Address 1");
        when(supplierService.saveSupplier(any(CreateSupplier.class))).thenReturn(supplier);

        // Act and Assert
        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\", \"name\":\"Supplier 1\",\"email\":\"email1@gmail.com\",\"phone\":\"123-456-7890\",\"address\":\"Address 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Supplier 1"))
                .andExpect(jsonPath("$.email").value("email1@gmail.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("Address 1"));
    }

    @Test
    void createSupplier_InvalidInput() throws Exception {
        // Arrange
        String invalidSupplierJson = "{\"name\":\"Supplier 1\",\"email\":\"Email 1\",\"phone\":\"Phone 1\",\"address\":\"Address 1\"}";
        doThrow(new InvalidInputException("Invalid data")).when(supplierService).saveSupplier(any(CreateSupplier.class));

        // Act & Assert
        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidSupplierJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void saveAllSuppliers() throws Exception {
        // Arrange
        List<CreateSupplier> suppliers = Arrays.asList(
                new CreateSupplier("1", "Supplier 1", "email1@gmail.com", "123-456-7890", "Address 1"),
                new CreateSupplier("2", "Supplier 2", "email2@gmail.com", "123-456-7890", "Address 2")
        );

        when(supplierService.saveAll(anyList())).thenReturn(suppliers);

        // Act and Assert
        mockMvc.perform(post("/suppliers/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\":\"Supplier 1\",\"email\":\"email1@gmail.com\",\"phone\":\"123-456-7890\",\"address\":\"Address 1\"}," +
                                "{\"name\":\"Supplier 2\",\"email\":\"email2@gmail.com\",\"phone\":\"123-456-7890\",\"address\":\"Address 2\"}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Supplier 1"))
                .andExpect(jsonPath("$[0].email").value("email1@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Supplier 2"))
                .andExpect(jsonPath("$[1].email").value("email2@gmail.com"))
                .andExpect(jsonPath("$[1].phone").value("123-456-7890"))
                .andExpect(jsonPath("$[1].address").value("Address 2"));
    }

    @Test
    void updateSupplier() throws Exception {
        // Arrange
        String id = "1";
        UpdateSupplier updateSupplier = new UpdateSupplier(id,"Supplier 1", "email1@gmail.com", "123-456-7890", "Address 1");
        when(supplierService.updateSupplier(eq(id), any(UpdateSupplier.class))).thenReturn(updateSupplier);

        // Act and Assert
        mockMvc.perform(put("/suppliers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Supplier 1\",\"email\":\"email1@gmail.com\",\"phone\":\"123-456-7890\",\"address\":\"Address 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Supplier 1"))
                .andExpect(jsonPath("$.email").value("email1@gmail.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("Address 1"));
    }

    @Test
    void updateSupplier_EntityNotFound() throws Exception {
        // Arrange
        String id = "1";
        UpdateSupplier updateSupplier = new UpdateSupplier(id,"Supplier 1", "email1@gmail.com", "123-456-7890", "Address 1");
        when(supplierService.updateSupplier(eq(id), any(UpdateSupplier.class))).thenThrow(new EntityNotFoundException(id));

        // Act and Assert
        mockMvc.perform(put("/suppliers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Supplier 1\",\"email\":\"email1@gmail.com\",\"phone\":\"123-456-7890\",\"address\":\"Address 1\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: " + id + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(supplierService, times(1)).updateSupplier(eq(id), any(UpdateSupplier.class));
    }

    @Test
    void updateSupplier_InvalidInput() throws Exception {
        // Arrange
        String id = "1";
        String invalidSupplierJson = "{\"name\":\"Supplier 1\",\"email\":\"Email 1\",\"phone\":\"Phone 1\",\"address\":\"Address 1\"}";

        // Act & Assert
        mockMvc.perform(put("/suppliers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidSupplierJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteSupplier() throws Exception {
        // Arrange
        String id = "1";
        doNothing().when(supplierService).deleteSupplier(eq(id));

        // Act & Assert
        mockMvc.perform(delete("/suppliers/{id}", id))
                .andExpect(status().isNoContent());

        verify(supplierService, times(1)).deleteSupplier(eq(id));
    }

    @Test
    void deleteSupplier_EntityNotFound() throws Exception {
        // Arrange
        String id = "1";
        doThrow(new EntityNotFoundException(id)).when(supplierService).deleteSupplier(id);

        // Act & Assert
        mockMvc.perform(delete("/suppliers/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity: " + id + " was not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(supplierService, times(1)).deleteSupplier(eq(id));
    }

    @Test
    void deleteAllSuppliers() throws Exception {
        // Arrange
        doNothing().when(supplierService).deleteAllSuppliers();

        // Act & Assert
        mockMvc.perform(delete("/suppliers"))
                .andExpect(status().isNoContent());

        verify(supplierService, times(1)).deleteAllSuppliers();
    }

    @Test
    void getProductCountsPerSupplier() throws Exception {
        // Arrange
        Map<String, Map<String, Long>> productCounts = Map.of(
                "Supplier 1", Map.of("Category 1", 10L, "Category 2", 5L),
                "Supplier 2", Map.of("Category 1", 7L, "Category 3", 3L)
        );

        when(supplierService.getFurnitureCountByCategoryAndSupplier()).thenReturn(productCounts);

        // Act and Assert
        mockMvc.perform(get("/suppliers/product-counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Supplier 1']['Category 1']").value(10))
                .andExpect(jsonPath("$.['Supplier 1']['Category 2']").value(5))
                .andExpect(jsonPath("$.['Supplier 2']['Category 1']").value(7))
                .andExpect(jsonPath("$.['Supplier 2']['Category 3']").value(3));
    }
}