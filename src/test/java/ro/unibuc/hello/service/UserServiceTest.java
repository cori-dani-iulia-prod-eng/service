package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.dto.UserUpdate;
import ro.unibuc.hello.enums.Role;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private User userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setName("John Doe");
        userEntity.setEmail("john@example.com");
        userEntity.setPhone("1234567890");

        userDto = new User(userEntity);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));

        // Act
        var users = userService.getAll();

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(userDto.getId(), users.get(0).getId());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

        // Act
        User user = userService.get("1");

        // Assert
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.get("1"));
        assertEquals("Entity: 1 was not found", exception.getMessage());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        UserUpdate updatedUser = new UserUpdate("1","Updated Name" ,"updated@example.com", "0987654321", Role.USER);
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        UserUpdate result = userService.update(updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhone());
        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        UserUpdate updatedUser = new UserUpdate("1", "Updated Name", "updated@example.com", "0987654321", Role.USER);
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.update(updatedUser));
        assertEquals("Entity: 1 was not found", exception.getMessage());
        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(0)).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUser_BlankFields() {
        // Arrange
        UserUpdate updatedUser = new UserUpdate("1", "", "", "", Role.USER);
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        UserUpdate result = userService.update(updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhone());
        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

        // Act
        userService.delete("1");

        // Assert
        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.delete("1"));
        assertEquals("Entity: 1 was not found", exception.getMessage());
        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(0)).delete(any(UserEntity.class));
    }

    @Test
    void testDeleteAllUsers() {
        // Act
        userService.deleteAll();

        // Assert
        verify(userRepository, times(1)).deleteAll();
    }
}
