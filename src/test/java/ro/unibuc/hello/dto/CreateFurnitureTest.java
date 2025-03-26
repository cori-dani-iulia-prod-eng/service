package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreateFurnitureTest {

    CreateFurniture myFurniture = new CreateFurniture("Table 022", "TABLE-022", 1, 100, 10, "Wood", "A table", "1234");

    @Test
    void test_name(){
        Assertions.assertSame("Table 022", myFurniture.getName());
    }
    @Test
    void test_sku(){
        Assertions.assertEquals("TABLE-022", myFurniture.getSku());
    }
    @Test
    void test_categoryCode(){
        Assertions.assertEquals(1, myFurniture.getCategoryCode());
    }
    @Test
    void test_price(){
        Assertions.assertEquals(100, myFurniture.getPrice());
    }
    @Test
    void test_stockQuantity(){
        Assertions.assertEquals(10, myFurniture.getStockQuantity());
    }
    @Test
    void test_material(){
        Assertions.assertSame("Wood", myFurniture.getMaterial());
    }
    @Test
    void test_description(){
        Assertions.assertSame("A table", myFurniture.getDescription());
    }
    @Test
    void test_supplierId(){
        Assertions.assertSame("1234", myFurniture.getSupplierId());
    }
}