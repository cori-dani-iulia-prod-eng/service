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

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ro.unibuc.hello.config.JwtUtil;
import ro.unibuc.hello.data.CategoryEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.AuthenticationRequest;
import ro.unibuc.hello.dto.CreateCategory;
import ro.unibuc.hello.dto.RegisterRequest;
import ro.unibuc.hello.dto.UpdateCategory;
import ro.unibuc.hello.service.AuthenticationService;
import ro.unibuc.hello.service.CategoryService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public class CategoryControllerIntegrationTest {

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
        final String MONGO_URL = "mongodb://host.docker.internal:";
        final String PORT = String.valueOf(mongoDBContainer.getMappedPort(27017));

        registry.add("mongodb.connection.url", () -> MONGO_URL + PORT);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void cleanUpAndAddTestData() throws Exception {
        categoryService.deleteAllCategories();
        userRepository.deleteAll();

        RegisterRequest registerRequest = new RegisterRequest("johhnyTest", "Copernic@1234", "Johnny Test", "email@gmail.com", "123-123-1234");
        authenticationService.register(registerRequest);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("johhnyTest", "Copernic@1234");
        token = jwtUtil.generateToken("johhnyTest");

        CreateCategory category1 = new CreateCategory(1, "Category 1");
        CreateCategory category2 = new CreateCategory(2, "Category 2");

        categoryService.saveCategory(category1);
        categoryService.saveCategory(category2);
    }

    @Test
    public void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/categories")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryCode").value(1))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].categoryCode").value(2))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    @Test
    public void testGetCategoryByCode() throws Exception {
        mockMvc.perform(get("/categories/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    public void testCreateCategory() throws Exception {
        CreateCategory category3 = new CreateCategory(3, "Category 3");

        mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category3)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryCode").value(3))
                .andExpect(jsonPath("$.name").value("Category 3"));

        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        UpdateCategory updatedCategory = new UpdateCategory(1, "Updated Category 1");

        mockMvc.perform(put("/categories/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.name").value("Updated Category 1"));

        mockMvc.perform(get("/categories/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryCode").value(1))
                .andExpect(jsonPath("$.name").value("Updated Category 1"));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testDeleteAllCategories() throws Exception {
        mockMvc.perform(delete("/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

}