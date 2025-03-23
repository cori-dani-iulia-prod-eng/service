package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UpdateFurnitureTest {

    UpdateFurniture myUpdateFurniture = new UpdateFurniture("Table 023", "TABLE-023", 1, 100, 10, "Wood", "A table", "1234");

    @Test
    void test_name(){
        Assertions.assertSame("Table 023", myUpdateFurniture.getName());
    }
    @Test
    void test_sku(){
        Assertions.assertEquals("TABLE-023", myUpdateFurniture.getSku());
    }
    @Test
    void test_categoryCode(){
        Assertions.assertEquals(1, myUpdateFurniture.getCategoryCode());
    }
    @Test
    void test_price(){
        Assertions.assertEquals(100, myUpdateFurniture.getPrice());
    }
    @Test
    void test_stockQuantity(){
        Assertions.assertEquals(10, myUpdateFurniture.getStockQuantity());
    }
    @Test
    void test_material(){
        Assertions.assertSame("Wood", myUpdateFurniture.getMaterial());
    }
    @Test
    void test_description(){
        Assertions.assertSame("A table", myUpdateFurniture.getDescription());
    }
    @Test
    void test_supplierId(){
        Assertions.assertSame("1234", myUpdateFurniture.getSupplierId());
    }
}