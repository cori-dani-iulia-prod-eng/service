package main.java.ro.unibuc.inventory_management.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stock_movement")
public class StockMovementEntity {
    @Id
    private String id;

    private int furnitureId;
    private int quantity;
    private date timestamp;

    public StockMovementEntity() {
    }

    public StockMovementEntity(int furnitureId, int quantity, date timestamp) {
        this.furnitureId = furnitureId;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public StockMovementEntity(String id, int furnitureId, int quantity, date timestamp) {
        this.id = id;
        this.furnitureId = furnitureId;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(int furnitureId) {
        this.furnitureId = furnitureId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "StockMovementEntity{" +
                "id='" + id + '\'' +
                ", furnitureId=" + furnitureId +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
