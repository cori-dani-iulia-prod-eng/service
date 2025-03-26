package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthenticationRequestTest {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "password");

    @Test
    void test_username() {
        Assertions.assertSame("username", authenticationRequest.getUsername());
    }

    @Test
    void test_password() {
        Assertions.assertSame("password", authenticationRequest.getPassword());
    }
}
