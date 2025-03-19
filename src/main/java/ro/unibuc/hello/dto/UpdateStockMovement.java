package ro.unibuc.hello.dto;

import jakarta.validation.constraints.Min;
import java.util.Date;

public class UpdateStockMovement {
    private String furnitureId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    private Date timestamp;

    public UpdateStockMovement() {
    }

    public UpdateStockMovement(String furnitureId, int quantity, Date timestamp) {
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
