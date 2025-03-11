package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "stock_movement")
public class StockMovementEntity {
    @Id
    private String id;


    private String furnitureId;
    private int quantity;
    private Date timestamp;

    public StockMovementEntity() {
    }


    public StockMovementEntity(String furnitureId, int quantity, Date timestamp) {

        this.furnitureId = furnitureId;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public StockMovementEntity(String id, String furnitureId, int quantity, Date timestamp) {

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

    public String getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(String furnitureId) {
        this.furnitureId = furnitureId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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
