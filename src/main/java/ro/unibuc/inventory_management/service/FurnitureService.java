package ro.unibuc.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.inventory_management.data.FurnitureEntity;
import ro.unibuc.inventory_management.data.FurnitureRepository;
import ro.unibuc.inventory_management.data.SupplierEntity;
import ro.unibuc.inventory_management.data.SupplierRepository;
import ro.unibuc.inventory_management.dto.Furniture;
import ro.unibuc.inventory_management.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FurnitureService {

    @Autowired
    private FurnitureRepository furnitureRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public Furniture getFurnitureBySku(String sku) throws EntityNotFoundException {
        Optional<FurnitureEntity> optionalEntity = furnitureRepository.findBySku(sku);
        FurnitureEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));
        return mapToDto(entity);
    }

    public List<Furniture> getAllFurniture() {
        List<FurnitureEntity> entities = furnitureRepository.findAll();
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Furniture saveFurniture(Furniture furnitureDto) {
        FurnitureEntity entity = mapToEntity(furnitureDto);
        furnitureRepository.save(entity);
        return mapToDto(entity);
    }

    public List<Furniture> saveAllFurniture(List<Furniture> furnitureDtos) {
        List<FurnitureEntity> entities = furnitureDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<FurnitureEntity> savedEntities = furnitureRepository.saveAll(entities);

        return savedEntities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Furniture updateFurniture(String sku, Furniture furnitureDto) throws EntityNotFoundException {
        FurnitureEntity entity = furnitureRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));
        entity.setName(furnitureDto.getName());
        entity.setCategoryCode(furnitureDto.getCategoryCode());
        entity.setPrice(furnitureDto.getPrice());
        entity.setStockQuantity(furnitureDto.getStockQuantity());
        entity.setMaterial(furnitureDto.getMaterial());
        entity.setDescription(furnitureDto.getDescription());

        // Update supplierId based on supplierName
        Optional<SupplierEntity> supplier = supplierRepository.findByName(furnitureDto.getSupplierName());
        if (supplier.isPresent()) {
            entity.setSupplierId(supplier.get().getId());
        }

        entity = furnitureRepository.save(entity);
        return mapToDto(entity);
    }

    public void deleteFurniture(String sku) throws EntityNotFoundException {
        FurnitureEntity entity = furnitureRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));
        furnitureRepository.delete(entity);
    }

    public void deleteAllFurniture() {
        furnitureRepository.deleteAll();
    }

    private Furniture mapToDto(FurnitureEntity entity) {
        String supplierName = supplierRepository.findById(entity.getSupplierId())
                .map(SupplierEntity::getName)
                .orElse("Unknown Supplier"); // Default if supplier not found

        return new Furniture(
                entity.getName(), entity.getSku(), entity.getCategoryCode(),
                entity.getPrice(), entity.getStockQuantity(), entity.getMaterial(),
                entity.getDescription(), supplierName);
    }

    private FurnitureEntity mapToEntity(Furniture dto) {
        String supplierId = supplierRepository.findByName(dto.getSupplierName())
                .map(SupplierEntity::getId)
                .orElse(null); // Null if supplier doesn't exist

        return new FurnitureEntity(
                dto.getName(), dto.getSku(), dto.getCategoryCode(),
                dto.getPrice(), dto.getStockQuantity(), dto.getMaterial(),
                dto.getDescription(), supplierId);
    }
    
    
}
