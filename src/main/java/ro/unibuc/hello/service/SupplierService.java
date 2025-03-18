package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.hello.data.SupplierRepository;
import ro.unibuc.hello.data.SupplierEntity;
import ro.unibuc.hello.dto.CreateSupplier;
import ro.unibuc.hello.dto.UpdateSupplier;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public List<CreateSupplier> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(entity -> new CreateSupplier(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
                .collect(Collectors.toList());
    }

    public CreateSupplier getSupplierByName(String name) throws EntityNotFoundException {
        Optional<SupplierEntity> optionalEntity = supplierRepository.findByName(name);
        SupplierEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException(name));
        return new CreateSupplier(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress());
    }

    public CreateSupplier saveSupplier(CreateSupplier createSupplier) {
        SupplierEntity entity = new SupplierEntity();
        entity.setName(createSupplier.getName());
        entity.setEmail(createSupplier.getEmail());
        entity.setPhone(createSupplier.getPhone());
        entity.setAddress(createSupplier.getAddress());
        supplierRepository.save(entity);
        return new CreateSupplier(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress());
    }

    public List<CreateSupplier> saveAll(List<CreateSupplier> createSuppliers) {
        List<SupplierEntity> entities = createSuppliers.stream()
                .map(supplier -> {
                    SupplierEntity entity = new SupplierEntity();
                    entity.setName(supplier.getName());
                    entity.setEmail(supplier.getEmail());
                    entity.setPhone(supplier.getPhone());
                    entity.setAddress(supplier.getAddress());
                    return entity;
                })
                .collect(Collectors.toList());

        List<SupplierEntity> savedEntities = supplierRepository.saveAll(entities);

        return savedEntities.stream()
                .map(entity -> new CreateSupplier(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
                .collect(Collectors.toList());
    }

    public UpdateSupplier updateSupplier(String id, UpdateSupplier updateSupplier) throws EntityNotFoundException {
        SupplierEntity entity = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        if(updateSupplier.getName() != null) {
            entity.setName(updateSupplier.getName());
        }

        if(updateSupplier.getEmail() != null) {
            entity.setEmail(updateSupplier.getEmail());
        }

        if(updateSupplier.getPhone() != null) {
            entity.setPhone(updateSupplier.getPhone());
        }

        if(updateSupplier.getAddress() != null) {
            entity.setAddress(updateSupplier.getAddress());
        }

        supplierRepository.save(entity);
        return new UpdateSupplier(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress());
    }

    public List<CreateSupplier> findSuppliersByName(String name) {
        return supplierRepository.findByName(name).stream()
                .map(entity -> new CreateSupplier(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
                .collect(Collectors.toList());
    }

    public List<CreateSupplier> findSuppliersByEmail(String email) {
        return supplierRepository.findByEmail(email).stream()
                .map(entity -> new CreateSupplier(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
                .collect(Collectors.toList());
    }

    public void deleteSupplier(String id) throws EntityNotFoundException {
        SupplierEntity entity = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        supplierRepository.delete(entity);
    }

    public void deleteAllSuppliers() {
        supplierRepository.deleteAll();
    }
}
