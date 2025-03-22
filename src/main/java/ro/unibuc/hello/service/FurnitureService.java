package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.hello.data.*;
import ro.unibuc.hello.dto.CreateFurniture;
import ro.unibuc.hello.dto.UpdateFurniture;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FurnitureService {

    @Autowired
    private FurnitureRepository furnitureRepository;

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public CreateFurniture getFurnitureBySku(String sku) throws EntityNotFoundException {
        Optional<FurnitureEntity> optionalEntity = furnitureRepository.findBySku(sku);
        FurnitureEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));
        return mapToCreateDto(entity);
    }

    public List<CreateFurniture> getAllFurniture() {
        List<FurnitureEntity> entities = furnitureRepository.findAll();
        return entities.stream()
                .map(this::mapToCreateDto)
                .collect(Collectors.toList());
    }

    public CreateFurniture saveFurniture(CreateFurniture furnitureDto) {
        if(!supplierRepository.existsById(furnitureDto.getSupplierId())) {
            throw new EntityNotFoundException("Supplier with ID " + furnitureDto.getSupplierId() + " not found");
        } else if(!categoryRepository.existsByCategoryCode(furnitureDto.getCategoryCode())) {
            throw new EntityNotFoundException("Category with code " + furnitureDto.getCategoryCode() + " not found");
        }
        FurnitureEntity entity = mapToEntity(furnitureDto);
        furnitureRepository.save(entity);
        return mapToCreateDto(entity);
    }

    public List<CreateFurniture> saveAllFurniture(List<CreateFurniture> furnitureDtos) {
        List<FurnitureEntity> entities = furnitureDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<FurnitureEntity> savedEntities = furnitureRepository.saveAll(entities);

        return savedEntities.stream()
                .map(this::mapToCreateDto)
                .collect(Collectors.toList());
    }

    public UpdateFurniture updateFurniture(String sku, UpdateFurniture furnitureDto) throws EntityNotFoundException {
        FurnitureEntity entity = furnitureRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));

        if(!supplierRepository.existsById(furnitureDto.getSupplierId())) {
            throw new EntityNotFoundException("Supplier with ID " + furnitureDto.getSupplierId() + " not found");
        } else if(!categoryRepository.existsByCategoryCode(furnitureDto.getCategoryCode())) {
            throw new EntityNotFoundException("Category with code " + furnitureDto.getCategoryCode() + " not found");
        }

        if (furnitureDto.getName() != null){
            entity.setName(furnitureDto.getName());
        }
        if (furnitureDto.getSku() != null){
            entity.setSku(furnitureDto.getSku());
        }
        if (furnitureDto.getDescription() != null){
            entity.setDescription(furnitureDto.getDescription());
        }
        if (furnitureDto.getCategoryCode() != 0){
            entity.setCategoryCode(furnitureDto.getCategoryCode());
        }
        if (furnitureDto.getPrice() != 0){
            entity.setPrice(furnitureDto.getPrice());
        }
        if (furnitureDto.getStockQuantity() != 0){
            entity.setStockQuantity(furnitureDto.getStockQuantity());
        }
        if (furnitureDto.getMaterial() != null){
            entity.setMaterial(furnitureDto.getMaterial());
        }

        // Update supplierId based on supplierName
        Optional<SupplierEntity> supplier = supplierRepository.findByName(furnitureDto.getSupplierId());
        if (supplier.isPresent()) {
            entity.setSupplierId(supplier.get().getId());
        }

        entity = furnitureRepository.save(entity);
        return mapToUpdateDto(entity);
    }

    public void deleteFurniture(String sku) throws EntityNotFoundException {
        FurnitureEntity entity = furnitureRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));
        furnitureRepository.delete(entity);
    }

    public void deleteAllFurniture() {
        furnitureRepository.deleteAll();
    }

    private CreateFurniture mapToCreateDto(FurnitureEntity entity) {

        return new CreateFurniture(
                entity.getName(), entity.getSku(), entity.getCategoryCode(),
                entity.getPrice(), entity.getStockQuantity(), entity.getMaterial(),
                entity.getDescription(), entity.getSupplierId());
    }

    private UpdateFurniture mapToUpdateDto(FurnitureEntity entity) {

        return new UpdateFurniture(
                entity.getName(), entity.getSku(), entity.getCategoryCode(),
                entity.getPrice(), entity.getStockQuantity(), entity.getMaterial(),
                entity.getDescription(), entity.getSupplierId());
    }

    private FurnitureEntity mapToEntity(CreateFurniture dto) {

        return new FurnitureEntity(
                dto.getName(), dto.getSku(), dto.getCategoryCode(),
                dto.getPrice(), dto.getStockQuantity(), dto.getMaterial(),
                dto.getDescription(), dto.getSupplierId());
    }

    // TODO Endpoint for low stock furniture or something else. Unit tests for all my features.
    
    
}
