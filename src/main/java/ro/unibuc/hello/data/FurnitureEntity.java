package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "furniture")
public class FurnitureEntity {

    @Id
    private String id;

    private String name;
    private String sku;
    private int categoryCode;
    private int price;
    private int stockQuantity;
    private String material;
    private String description;
    private String supplierId;

    public FurnitureEntity() {}

    public FurnitureEntity(String name, String sku, int categoryCode, int price, int stockQuantity, String material, String description, String supplierId) {
        this.name = name;
        this.sku = sku;
        this.categoryCode = categoryCode;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.description = description;
        this.supplierId = supplierId;
    }

    public FurnitureEntity(String id, String name, String sku, int categoryCode, int price, int stockQuantity, String material, String description, String supplierId) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.categoryCode = categoryCode;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.description = description;
        this.supplierId = supplierId;
    }

    public String getId() {
        return id;
    }

    // It is not used in the code, mongoDB will generate the id
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

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        
        return String.format(
                "Furniture[id='%s', name='%s', sku='%s', categoryCode='%s', price='%s', stockQuantity='%s', material='%s', description='%s', supplierId='%s']",
                id, name, sku, categoryCode, price, stockQuantity, material, description, supplierId);
    }

    
}
