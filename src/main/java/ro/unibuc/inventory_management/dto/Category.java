package ro.unibuc.inventory_management.dto;

public class Category {

    private String categoryCode;
    private String name;

    public Category() {}

    public Category(String code, String name) {
        this.categoryCode = code;
        this.name = name;
    }

    public void setCategoryCode(String code) {
        this.categoryCode = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public String getName() {
        return name;
    }     
    
}
