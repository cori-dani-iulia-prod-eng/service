package ro.unibuc.hello.dto;

import jakarta.validation.constraints.*;

public class UpdateFurniture {

    private String name;

    @Pattern(regexp = "^[A-Z]+-\\d{3}$", message = "SKU must contain only letters and numbers")
    private String sku;

    @Min(value = 1, message = "Category code must be at least 1")
    private int categoryCode;

    @Min(value = 1, message = "Price must be at least 1")
    private int price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    private String material;

    private String description;

    private String supplierId;

    public UpdateFurniture() {}

    public UpdateFurniture(String name, String sku, int categoryCode, int price, int stockQuantity, String material, String description, String supplierId) {
        this.name = name;
        this.sku = sku;
        this.categoryCode = categoryCode;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.description = description;
        this.supplierId = supplierId;
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

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
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

    public String getSupplierId() {
        return supplierId;
    }
}
