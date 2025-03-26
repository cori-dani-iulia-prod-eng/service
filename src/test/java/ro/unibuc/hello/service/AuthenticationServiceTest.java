package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.hello.config.JwtUtil;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.AuthenticationRequest;
import ro.unibuc.hello.dto.AuthenticationResponse;
import ro.unibuc.hello.dto.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequest = new RegisterRequest("username", "password", "name", "email@123",  "phone");
        authenticationRequest = new AuthenticationRequest("username", "password");
    }

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        when(userRepository.existsByUsernameOrEmailOrPhone(anyString(), anyString(), anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity(registerRequest));
        when(jwtUtil.generateToken(any(UserEntity.class))).thenReturn("mockJwtToken");

        // Act
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        Mockito.verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegister_UserAlreadyExists() throws Exception {
        // Arrange
        when(userRepository.existsByUsernameOrEmailOrPhone(anyString(), anyString(), anyString())).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> authenticationService.register(registerRequest));
        assertEquals("User already exists", exception.getMessage());
        Mockito.verify(userRepository, times(0)).save(any(UserEntity.class));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        UserEntity userEntity = new UserEntity(registerRequest);
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(userEntity));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtUtil.generateToken(any(UserEntity.class))).thenReturn("mockJwtToken");

        // Act
        AuthenticationResponse response = authenticationService.login(authenticationRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        Mockito.verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> authenticationService.login(authenticationRequest));
        assertEquals("username", exception.getMessage());
    }
}
