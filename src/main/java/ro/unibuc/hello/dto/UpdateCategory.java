package ro.unibuc.hello.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateCategory {

    @Min(value=1, message = "Category code should be at least 1")
    private int categoryCode;

    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String name;

    public UpdateCategory() {}

    public UpdateCategory(int code, String name) {
        this.categoryCode = code;
        this.name = name;
    }

    public void setCategoryCode(int code) {
        this.categoryCode = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public String getName() {
        return name;
    }
}
