package ro.unibuc.inventory_management.dto;

public class Furniture {
    
    private String id;
    private String name;
    private String sku;
    private int categoryCode;
    private int price;
    private int stockQuantity;
    private String material;
    private String description;

    public Furniture() {}

    public Furniture(String id, String name, String sku, int categoryCode, int price, int stockQuantity, String material, String description) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.categoryCode = categoryCode;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getId() {
        return id;
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
}
