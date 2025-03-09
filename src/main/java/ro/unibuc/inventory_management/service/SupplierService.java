package ro.unibuc.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.inventory_management.data.SupplierRepository;
import ro.unibuc.inventory_management.data.SupplierEntity;
import ro.unibuc.inventory_management.dto.Supplier;
import ro.unibuc.inventory_management.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(entity -> new Supplier(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
                .collect(Collectors.toList());
    }

    public Supplier getSupplierByName(String name) throws EntityNotFoundException {
        Optional<SupplierEntity> optionalEntity = supplierRepository.findByName(name);
        SupplierEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException(name));
        return new Supplier(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress());
    }

    public Supplier saveSupplier(Supplier supplier) {
        SupplierEntity entity = new SupplierEntity();
        entity.setName(supplier.getName());
        entity.setEmail(supplier.getEmail());
        entity.setPhone(supplier.getPhone());
        entity.setAddress(supplier.getAddress());
        supplierRepository.save(entity);
        return new Supplier(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress());
    }

    public List<Supplier> saveAll(List<Supplier> suppliers) {
        List<SupplierEntity> entities = suppliers.stream()
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
                .map(entity -> new Supplier(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
                .collect(Collectors.toList());
    }

    public Supplier updateSupplier(String id, Supplier supplier) throws EntityNotFoundException {
        SupplierEntity entity = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        entity.setName(supplier.getName());
        entity.setEmail(supplier.getEmail());
        entity.setPhone(supplier.getPhone());
        entity.setAddress(supplier.getAddress());
        supplierRepository.save(entity);
        return new Supplier(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress());
    }

    public List<Supplier> findSuppliersByName(String name) {
        return supplierRepository.findByName(name).stream()
                .map(entity -> new Supplier(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
                .collect(Collectors.toList());
    }

    public List<Supplier> findSuppliersByEmail(String email) {
        return supplierRepository.findByEmail(email).stream()
                .map(entity -> new Supplier(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress()))
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
