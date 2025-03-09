package ro.unibuc.inventory_management.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public class CategoryEntity {

    @Id
    private String mongoId; // MongoDB-generated ID

    private String id; // Custom-defined ID
    private String name;

    public CategoryEntity() {}

    public CategoryEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryEntity(String mongoId, String id, String name) {
        this.mongoId = mongoId;
        this.id = id;
        this.name = name;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
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

    @Override
    public String toString() {
        return String.format(
                "Category[mongoId='%s', id='%s', name='%s']",
                mongoId, id, name);
    }
}