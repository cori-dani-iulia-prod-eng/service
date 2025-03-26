package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ro.unibuc.hello.enums.Role;

public class UserTest {
    User user = new User("123", "User1", "user1@gmail.com", "123-123-1234", Role.USER);

    @Test
    void test_id() {
        Assertions.assertSame("123", user.getId());
    }

    @Test
    void test_name() {
        Assertions.assertSame("User1", user.getName());
    }

    @Test
    void test_email() {
        Assertions.assertSame("user1@gmail.com", user.getEmail());
    }

    @Test
    void test_phone() {
        Assertions.assertSame("123-123-1234", user.getPhone());
    }

    @Test
    void test_role() {
        Assertions.assertSame(Role.USER, user.getRole());
    }
}

