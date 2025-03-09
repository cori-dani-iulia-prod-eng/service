package ro.unibuc.inventory_management.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "furniture")
public class FurnitureEntity {

    @Id
    private String id;

    private String name;
    private String sku;
    private int categoryId;
    private int price;
    private int stockQuantity;
    private String material;
    private String description;

    public FurnitureEntity() {}

    public FurnitureEntity(String name, String sku, int categoryId, int price, int stockQuantity, String material, String description) {
        this.name = name;
        this.sku = sku;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.description = description;
    }

    public FurnitureEntity(String id, String name, String sku, int categoryId, int price, int stockQuantity, String material, String description) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "Furniture[id='%s', name='%s', sku='%s', categoryId='%s', price='%s', stockQuantity='%s', material='%s', description='%s']",
                id, name, sku, categoryId, price, stockQuantity, material, description);
    }

    
}
