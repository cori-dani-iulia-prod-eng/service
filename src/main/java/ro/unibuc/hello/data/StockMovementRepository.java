package ro.unibuc.hello.data;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementRepository extends MongoRepository<StockMovementEntity, String>{
    
    List<StockMovementEntity> findByFurnitureId(String furnitureId);
    Optional<StockMovementEntity> findById(String id);
    List<StockMovementEntity> findByQuantity(int quantity);
    List<StockMovementEntity> findByTimestamp(Date timestamp);
}
