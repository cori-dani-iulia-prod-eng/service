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

    /**
     * Get a furniture by its SKU
     * @param sku the SKU of the furniture
     * @return the furniture with the given SKU
     */
    public CreateFurniture getFurnitureBySku(String sku) throws EntityNotFoundException {
        Optional<FurnitureEntity> optionalEntity = furnitureRepository.findBySku(sku);
        FurnitureEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));
        return mapToCreateDto(entity);
    }

    /**
     * Get all furniture
     * @return list of all furniture
     */
    public List<CreateFurniture> getAllFurniture() {
        List<FurnitureEntity> entities = furnitureRepository.findAll();
        return entities.stream()
                .map(this::mapToCreateDto)
                .collect(Collectors.toList());
    }

    /**
     * Save a new furniture
     * @param furnitureDto the furniture to be saved DTO
     * @return the saved furniture
     */
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

    /**
     * Save a list of new furniture
     * @param furnitureDtos the list of furniture to be saved DTOs
     * @return the saved furniture
     */
    public List<CreateFurniture> saveAllFurniture(List<CreateFurniture> furnitureDtos) {
        List<FurnitureEntity> entities = furnitureDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<FurnitureEntity> savedEntities = furnitureRepository.saveAll(entities);

        return savedEntities.stream()
                .map(this::mapToCreateDto)
                .collect(Collectors.toList());
    }

    /**
     * Update a furniture by its SKU
     * @param sku the SKU of the furniture
     * @param furnitureDto the furniture to be updated DTO
     * @return the updated furniture
     */
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

    /**
     * Delete a furniture by its SKU
     * @param sku the SKU of the furniture
     */
    public void deleteFurniture(String sku) throws EntityNotFoundException {
        FurnitureEntity entity = furnitureRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Furniture with SKU " + sku + " not found"));
        furnitureRepository.delete(entity);
    }

    /**
     * Delete all furniture
     */
    public void deleteAllFurniture() {
        furnitureRepository.deleteAll();
    }

    /**
     * Map a furniture entity to a create furniture DTO
     * @param entity the furniture entity
     * @return the create furniture DTO
     */
    private CreateFurniture mapToCreateDto(FurnitureEntity entity) {

        return new CreateFurniture(
                entity.getName(), entity.getSku(), entity.getCategoryCode(),
                entity.getPrice(), entity.getStockQuantity(), entity.getMaterial(),
                entity.getDescription(), entity.getSupplierId());
    }

    /** Map a furniture entity to an update furniture DTO
     * @param entity the furniture entity
     * @return the update furniture DTO
     */
    private UpdateFurniture mapToUpdateDto(FurnitureEntity entity) {

        return new UpdateFurniture(
                entity.getName(), entity.getSku(), entity.getCategoryCode(),
                entity.getPrice(), entity.getStockQuantity(), entity.getMaterial(),
                entity.getDescription(), entity.getSupplierId());
    }

    /**
     * Map a create furniture DTO to a furniture entity
     * @param dto the create furniture DTO
     * @return the furniture entity
     */
    private FurnitureEntity mapToEntity(CreateFurniture dto) {

        return new FurnitureEntity(
                dto.getName(), dto.getSku(), dto.getCategoryCode(),
                dto.getPrice(), dto.getStockQuantity(), dto.getMaterial(),
                dto.getDescription(), dto.getSupplierId());
    }

    /**
     * Get the total number of furniture with stock quantity below a given threshold
     * @param stockThreshold the threshold for low stock
     * @return the total number of furniture with stock quantity below the threshold
     */
    public int getTotalLowStockFurniture(int stockThreshold) {

        List<FurnitureEntity> allFurniture = furnitureRepository.findAll();

        return (int) allFurniture.stream()
                .filter(furniture -> furniture.getStockQuantity() < stockThreshold)
                .count();
    }

    /**
     * Get a list of furniture with stock quantity below a given threshold
     * @param stockThreshold the threshold for low stock
     * @return list of furniture with stock quantity below the threshold
     */
    public List<CreateFurniture> getLowStockFurniture(int stockThreshold) {

        List<FurnitureEntity> allFurniture = furnitureRepository.findAll();

        List<FurnitureEntity> lowStockFurniture = allFurniture.stream()
                .filter(furniture -> furniture.getStockQuantity() < stockThreshold)
                .toList();

        return lowStockFurniture.stream()
                .map(this::mapToCreateDto)
                .collect(Collectors.toList());
    }


}
