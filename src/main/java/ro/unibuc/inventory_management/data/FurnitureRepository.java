package ro.unibuc.inventory_management.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FurnitureRepository extends MongoRepository<FurnitureEntity, String>{

    FurnitureEntity findByName(String name);
    List<FurnitureEntity> findByMaterial(String material);
    List<FurnitureEntity> findByDescription(String description);
    List<FurnitureEntity> findByCategoryId(int categoryId);
    List<FurnitureEntity> findByPrice(int price);
    List<FurnitureEntity> findBySku(String sku);
    List<FurnitureEntity> findByStockQuantity(int stockQuantity);


}
