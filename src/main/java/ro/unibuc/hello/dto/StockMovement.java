package ro.unibuc.hello.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class StockMovement {
    @NotNull(message = "Furniture ID is mandatory")
    private String furnitureId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull(message = "Timestamp is mandatory")
    private Date timestamp;

    public StockMovement() {
    }

    public StockMovement(String furnitureId, int quantity, Date timestamp) {
        this.furnitureId = furnitureId;
        this.quantity = quantity;
        this.timestamp = timestamp;
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
}
