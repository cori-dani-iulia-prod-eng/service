package ro.unibuc.inventory_management.dto;

public class Furniture {
    
    private String id;
    private String name;
    private String sku;
    private int categoryId;
    private int price;
    private int stockQuantity;
    private String material;
    private String description;

    public Furniture() {}

    public Furniture(String id, String name, String sku, int categoryId, int price, int stockQuantity, String material, String description) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.categoryId = categoryId;
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

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
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
