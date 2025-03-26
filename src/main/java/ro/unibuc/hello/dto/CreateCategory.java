package ro.unibuc.hello.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCategory {

    @NotNull(message = "Category code cannot be null")
    private int categoryCode;

    @NotNull(message = "Category name cannot be null")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String name;

    public CreateCategory() {}

    public CreateCategory(int code, String name) {
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
