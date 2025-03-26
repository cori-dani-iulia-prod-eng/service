package ro.unibuc.hello.dto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class UpdateStockMovementTest {
    UpdateStockMovement myStockMovement = new UpdateStockMovement("1", 10, new Date(1700000000000L));

    @Test
    void test_furnitureId() {
        Assertions.assertSame("1", myStockMovement.getFurnitureId());
    }

    @Test
    void test_quantity() {
        Assertions.assertSame(10, myStockMovement.getQuantity());
    }

    @Test
    void test_timestamp() {
        Assertions.assertEquals(new Date(1700000000000L), myStockMovement.getTimestamp());
    }
}
