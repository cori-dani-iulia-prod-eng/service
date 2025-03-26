package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.AuthenticationRequest;
import ro.unibuc.hello.dto.AuthenticationResponse;
import ro.unibuc.hello.dto.RegisterRequest;
import ro.unibuc.hello.filters.GlobalExceptionFilter;
import ro.unibuc.hello.service.AuthenticationService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).setControllerAdvice(new GlobalExceptionFilter()).build();
    }

    @Test
    public void test_register() throws Exception {
        Mockito.when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(new AuthenticationResponse("mock-jwt-token"));

        String registerRequestJson = "{" +
                "\"username\":\"user1\"," +
                "\"password\":\"pass123\"," +
                "\"name\":\"Test User\"," +
                "\"email\":\"test@example.com\"," +
                "\"phone\":\"123-456-7890\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    public void test_login() throws Exception {
        Mockito.when(authenticationService.login(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse("mock-jwt-token"));

        String loginRequestJson = "{" +
                "\"username\":\"user1\"," +
                "\"password\":\"pass123\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    public void test_login_invalidInput() throws Exception {
        String invalidLoginRequestJson = "{" +
                "\"username\":\"\"," +
                "\"password\":\"\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLoginRequestJson))
                        .andExpect(status().isBadRequest());
    }
}
