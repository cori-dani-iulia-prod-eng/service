package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreateCategoryTest {

    CreateCategory myCategory = new CreateCategory(7, "Accessories");

    @Test
    void test_categoryCode(){
        Assertions.assertEquals(7, myCategory.getCategoryCode());
    }

    @Test
    void test_name(){
        Assertions.assertSame("Accessories", myCategory.getName());
    }
}