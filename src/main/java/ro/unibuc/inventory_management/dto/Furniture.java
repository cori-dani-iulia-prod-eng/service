package ro.unibuc.inventory_management.dto;

import jakarta.validation.constraints.*;

public class Furniture {
    
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "SKU cannot be empty and must follow the pattern 'categoryName-XXX'")
    @Pattern(regexp = "^[A-Z]+-\\d{3}$", message = "SKU must contain only letters and numbers")
    private String sku;

    @Min(value = 1, message = "Category code must be at least 1")
    @NotBlank(message = "Category code cannot be empty")
    private int categoryCode;

    @NotBlank(message = "Price cannot be empty")
    @Min(value = 1, message = "Price must be at least 1")
    private int price;

    @NotBlank(message = "Stock quantity cannot be empty")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @NotBlank(message = "Material cannot be empty")
    private String material;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    private String supplierName;

    public Furniture() {}

    public Furniture(String name, String sku, int categoryCode, int price, int stockQuantity, String material, String description, String supplierName) {
        this.name = name;
        this.sku = sku;
        this.categoryCode = categoryCode;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.description = description;
        this.supplierName = supplierName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public int getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getMaterial() {
        return material;
    }

    public String getDescription() {
        return description;
    }

    public String getSupplierName() {
        return supplierName;
    }
}
