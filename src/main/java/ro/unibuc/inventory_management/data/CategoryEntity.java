package ro.unibuc.inventory_management.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public class CategoryEntity {

    @Id
    private String id; 

    private int categoryCode; 
    private String name;

    public CategoryEntity() {}

    public CategoryEntity(int code, String name) {
        this.categoryCode = code;
        this.name = name;
    }

    public CategoryEntity(String id, int code, String name) {
        this.id = id;
        this.categoryCode = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int code) {
        this.categoryCode = code;
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
                "Category[id='%s', categoryCode='%d', name='%s']",
                id, categoryCode, name);
    }
}