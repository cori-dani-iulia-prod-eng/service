package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.dto.UserUpdate;
import ro.unibuc.hello.enums.Role;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void test_getAll() throws Exception {
        List<User> users = Arrays.asList(
                new User("123", "Name1", "email1@gmail.com", "123-123-123", Role.USER),
                new User("123", "Name2", "email2@gmail.com", "123-123-123", Role.USER)
        );
        Mockito.when(userService.getAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[0].name").value("Name1"))
                .andExpect(jsonPath("$[0].email").value("email1@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("123-123-123"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[1].id").value("123"))
                .andExpect(jsonPath("$[1].name").value("Name2"))
                .andExpect(jsonPath("$[1].email").value("email2@gmail.com"))
                .andExpect(jsonPath("$[1].phone").value("123-123-123"))
                .andExpect(jsonPath("$[1].role").value("USER"));
    }

    @Test
    public void test_getById() throws Exception {
        User user = new User("123", "Name1", "email1@gmail.com", "123-123-123", Role.USER);
        Mockito.when(userService.get("123")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Name1"));
    }


    @Test
    public void test_update_success() throws Exception {
        UserUpdate updatedUser = new UserUpdate("123", "UpdatedName", "updatedemail@gmail.com", "123-123-1234", Role.ADMIN);
        Mockito.when(userService.update(any(UserUpdate.class))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"123\", \"name\": \"UpdatedName\", \"email\": \"updatedemail@gmail.com\", \"phone\": \"123-123-1234\", \"role\": \"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

//    @Test
//    public void test_update_invalid() throws Exception {
//        UserUpdate updatedUser = new UserUpdate("123", "UpdatedName", "updatedemail@gmail.com", "123-123-1234", Role.ADMIN);
//        Mockito.when(userService.update(any(UserUpdate.class))).thenReturn(updatedUser);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\": \"123\", \"name\": \"UpdatedName\", \"email\": \"updatedemail@gmail.com\", \"phone\": \"123-123-12\", \"role\": \"ADMIN\"}"))
//                .andExpect(result -> assertInstanceOf(InvalidInputException.class, result.getResolvedException()));
//    }

    @Test
    void test_deleteUser() throws Exception {
        String id = "123";
        UserUpdate user = new UserUpdate(id, "Name1", "email1@gmail.com", "123-123-123", Role.USER);
        Mockito.when(userService.update(any(UserUpdate.class))).thenReturn(user);

        // Add user
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"123\", \"name\": \"Name1\", \"email\": \"email1@gmail.com\", \"phone\": \"123-123-1234\", \"role\": \"USER\"}"))
                .andExpect(status().isOk());

        // Delete user
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).delete(id);

        // Check if user is deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void test_deleteAll() throws Exception {
        String id = "123";
        UserUpdate user = new UserUpdate(id, "Name1", "email1@gmail.com", "123-123-123", Role.USER);
        Mockito.when(userService.update(any(UserUpdate.class))).thenReturn(user);

        // Add user
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"123\", \"name\": \"Name1\", \"email\": \"email1@gmail.com\", \"phone\": \"123-123-1234\", \"role\": \"USER\"}"))
                        .andExpect(status().isOk());

        // Delete all users
        mockMvc.perform(MockMvcRequestBuilders.delete("/users"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteAll();

        // Check if all users are deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
