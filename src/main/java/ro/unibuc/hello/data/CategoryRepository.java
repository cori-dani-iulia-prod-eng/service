package ro.unibuc.hello.data;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryEntity, String>{
    
    CategoryEntity findByName(String name);
    Optional<CategoryEntity> findByCategoryCode(int code);
}
