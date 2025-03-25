package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CreateSupplierTest {
    CreateSupplier mySupplier = new CreateSupplier("1", "Nume", "emailsupplier@gmail.com", "123-456-7890", "Str. Test");

    @Test
    void test_id() {
        Assertions.assertSame("1", mySupplier.getId());
    }

    @Test
    void test_name() {
        Assertions.assertEquals("Nume", mySupplier.getName());
    }

    @Test
    void test_email() {
        Assertions.assertEquals("emailsupplier@gmail.com", mySupplier.getEmail());
    }

    @Test
    void test_phone() {
        Assertions.assertEquals("123-456-7890", mySupplier.getPhone());
    }

    @Test
    void test_address() {
        Assertions.assertEquals("Str. Test", mySupplier.getAddress());
    }
}
