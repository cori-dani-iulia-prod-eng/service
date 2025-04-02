package ro.unibuc.hello.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ro.unibuc.hello.config.JwtUtil;
import ro.unibuc.hello.dto.AuthenticationRequest;
import ro.unibuc.hello.dto.RegisterRequest;
import ro.unibuc.hello.service.AuthenticationService;
import ro.unibuc.hello.service.UserService;


import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public class AuthenticationControllerIntegrationTest {
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017)
            .withSharding();

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

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void cleanUpAndAddTestData(){
        userService.deleteAll();
    }

    @Test
    public void testRegister()  throws Exception  {
        RegisterRequest user = new RegisterRequest("user1", "password1", "name1", "email1@gmail.com", "123-123-1234");

        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())  // Ensure the token is returned
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String token = new ObjectMapper().readTree(responseBody).get("token").asText();

        mockMvc.perform(get("/users").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].email").value("email1@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("123-123-1234"))
                .andExpect(jsonPath("$[0].role").value("USER"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isInternalServerError())  // Expect 400 Bad Request
                .andExpect(jsonPath("$.message").value("An unexpected error occurred:User already exists"));
    }

    @Test
    public void testLogin() throws Exception {
        // Register the user first
        RegisterRequest user = new RegisterRequest("user1", "password1", "name1", "email1@gmail.com", "123-123-1234");
        authenticationService.register(user);

        AuthenticationRequest invalidRequest = new AuthenticationRequest("", "");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", matchesPattern("^Invalid input:.*")));

        // Create a login request
        AuthenticationRequest loginRequest = new AuthenticationRequest("user1", "password1");

        // Perform the login request and extract the token
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())  // Ensure the token is returned
                .andReturn();

        // Extract token from response
        String responseBody = result.getResponse().getContentAsString();
        String token = new ObjectMapper().readTree(responseBody).get("token").asText();

        // Use the token to access a protected resource (e.g., /users)
        mockMvc.perform(get("/users").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].email").value("email1@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("123-123-1234"))
                .andExpect(jsonPath("$[0].role").value("USER"));
    }



}
