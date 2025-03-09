package ro.unibuc.hello.data;


import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends MongoRepository<SupplierEntity, String>{
    
    Optional<SupplierEntity> findByName(String name);
    Optional<SupplierEntity> findById(String id);
    List<SupplierEntity> findByEmail(String email);
    List<SupplierEntity> findByPhone(String phone);
    List<SupplierEntity> findByAddress(String address);
}
