package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UpdateCategoryTest {

    UpdateCategory myUpdateCategory = new UpdateCategory(7, "Accessories");

    @Test
    void test_categoryCode(){
        Assertions.assertEquals(7, myUpdateCategory.getCategoryCode());
    }
    @Test
    void test_name(){
        Assertions.assertSame("Accessories", myUpdateCategory.getName());
    }
}