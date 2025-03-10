package main.java.ro.unibuc.inventory_management.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementRepository extends MongoRepository<StockMovementEntity, String>{
    
    StockMovementEntity findByFurnitureId(int furnitureId);
    Optional<StockMovementEntity> findById(String id);
    List<StockMovementEntity> findByQuantity(int quantity);
    List<StockMovementEntity> findByTimestamp(date timestamp);
}
