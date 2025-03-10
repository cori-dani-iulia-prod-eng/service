package ro.unibuc.hello.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FurnitureRepository extends MongoRepository<FurnitureEntity, String>{

    Optional<FurnitureEntity> findById(String id);
    FurnitureEntity findByName(String name);
    List<FurnitureEntity> findByMaterial(String material);
    List<FurnitureEntity> findByDescription(String description);
    List<FurnitureEntity> findByCategoryCode(int categoryId);
    List<FurnitureEntity> findByPrice(int price);
    Optional<FurnitureEntity> findBySku(String sku);
    List<FurnitureEntity> findByStockQuantity(int stockQuantity);
    List<FurnitureEntity> findBySupplierId(String supplierId);
    List<FurnitureEntity> findBySupplierName(String supplierName);


}
