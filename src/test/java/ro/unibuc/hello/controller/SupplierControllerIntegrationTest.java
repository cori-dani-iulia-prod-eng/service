package ro.unibuc.hello.controller;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ro.unibuc.hello.config.JwtUtil;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.*;
import ro.unibuc.hello.service.AuthenticationService;
import ro.unibuc.hello.service.StockMovementService;
import ro.unibuc.hello.service.SupplierService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public class SupplierControllerIntegrationTest {
    @SuppressWarnings("resource")
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017)
            .withSharding();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeAll
    public static void setUp() {
        mongoDBContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        final String MONGO_URL = "mongodb://localhost:";
        final String PORT = String.valueOf(mongoDBContainer.getMappedPort(27017));

        registry.add("mongodb.connection.url", () -> MONGO_URL + PORT);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @Autowired
    private SupplierService supplierService;

    @BeforeEach
    public void cleanUpAndAddTestData() throws Exception {
        supplierService.deleteAllSuppliers();
        userRepository.deleteAll();

        RegisterRequest registerRequest = new RegisterRequest("johhnyTest", "Copernic@1234", "Johnny Test", "email@gmail.com", "123-123-1234");
        authenticationService.register(registerRequest);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("johhnyTest", "Copernic@1234");
        token = jwtUtil.generateToken("johhnyTest");

        CreateSupplier supplier1 = new CreateSupplier("1", "Supplier 1", "email1@gmail.com", "123-456-7890", "Address 1");
        CreateSupplier supplier2 = new CreateSupplier("2", "Supplier 2", "email2@gmail.com", "123-456-7890", "Address 2");

        supplierService.saveSupplier(supplier1);
        supplierService.saveSupplier(supplier2);
    }


    @Test
    public void testGetAllSupplier() throws Exception {
        mockMvc.perform(get("/suppliers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Supplier 1"))
                .andExpect(jsonPath("$[0].email").value("email1@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[1].name").value("Supplier 2"))
                .andExpect(jsonPath("$[1].email").value("email2@gmail.com"))
                .andExpect(jsonPath("$[1].phone").value("123-456-7890"))
                .andExpect(jsonPath("$[1].address").value("Address 2"));
    }

    @Test
    public void testGetSupplierByName() throws Exception {
        String name = "Supplier 1";
        mockMvc.perform(get("/suppliers/{name}", name)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Supplier 1"))
                .andExpect(jsonPath("$.email").value("email1@gmail.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("Address 1"));
    }

    @Test
    public void testCreateSupplier() throws Exception {
        CreateSupplier supplier3 = new CreateSupplier("3", "Supplier 3", "email3@gmail.com", "123-456-7890", "Address 3");

        mockMvc.perform(post("/suppliers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(supplier3)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Supplier 3"))
                .andExpect(jsonPath("$.email").value("email3@gmail.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("Address 3"));

        mockMvc.perform(get("/suppliers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testSaveAllSuppliers() throws Exception {
        List<CreateSupplier> suppliers = Arrays.asList(
                new CreateSupplier("3", "Supplier 3", "email3@gmail.com", "123-456-7890", "Address 3"),
                new CreateSupplier("4", "Supplier 4", "email4@gmail.com", "123-456-7890", "Address 4"),
                new CreateSupplier("5", "Supplier 5", "email5@gmail.com", "123-456-7890", "Address 5")
        );

        mockMvc.perform(post("/suppliers/batch")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(suppliers)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));

        mockMvc.perform(get("/suppliers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void testUpdateSupplier() throws Exception {
        CreateSupplier supplier = supplierService.getSupplierByName("Supplier 1");

        String id = supplier.getId();
        String updatedName = "Updated Supplier 1";
        UpdateSupplier updatedSupplier = new UpdateSupplier("1", "Updated Supplier 1", "updatedemail1@gmail.com", "123-456-7890", "Updated Address 1");

        mockMvc.perform(put("/suppliers/{id}",id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedSupplier)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Supplier 1"))
                .andExpect(jsonPath("$.email").value("updatedemail1@gmail.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("Updated Address 1"));

        mockMvc.perform(get("/suppliers/{updatedName}", updatedName)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Supplier 1"))
                .andExpect(jsonPath("$.email").value("updatedemail1@gmail.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("Updated Address 1"));
    }

    @Test
    public void testDeleteSupplier() throws Exception {
        CreateSupplier supplier = supplierService.getSupplierByName("Supplier 1");

        String id = supplier.getId();

        mockMvc.perform(delete("/suppliers/{id}", id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/suppliers/{id}", id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/suppliers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testDeleteAllSuppliers() throws Exception {
        mockMvc.perform(delete("/suppliers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/suppliers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

}
