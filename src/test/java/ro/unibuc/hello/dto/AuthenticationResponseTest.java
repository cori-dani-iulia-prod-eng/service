package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthenticationResponseTest {
    AuthenticationResponse authenticationRequest = new AuthenticationResponse("token123");

    @Test
    void test_token() {
        Assertions.assertSame("token123", authenticationRequest.getToken());
    }
}
