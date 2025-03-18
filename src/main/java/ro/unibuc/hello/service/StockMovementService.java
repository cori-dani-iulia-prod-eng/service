package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import ro.unibuc.hello.data.FurnitureRepository;
import ro.unibuc.hello.data.StockMovementRepository;
import ro.unibuc.hello.data.StockMovementEntity;
import ro.unibuc.hello.dto.CreateStockMovement;
import ro.unibuc.hello.dto.UpdateStockMovement;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

@Component
public class StockMovementService {
        @Autowired
    private StockMovementRepository stock_movementRepository;

        @Autowired
        private FurnitureRepository furnitureRepository;

    public List<CreateStockMovement> getAllStockMovements() {
        return stock_movementRepository.findAll().stream()
                .map(entity -> new CreateStockMovement(entity.getFurnitureId(), entity.getQuantity(),  entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    public CreateStockMovement getStockMovementById(String id) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return new CreateStockMovement(entity.getFurnitureId(), entity.getQuantity(),  entity.getTimestamp());
    }

    public CreateStockMovement saveStockMovement(CreateStockMovement stock_movementDto) {
        if(!furnitureRepository.existsById(stock_movementDto.getFurnitureId())) {
            throw new EntityNotFoundException("Furniture with ID " + stock_movementDto.getFurnitureId() + " not found");
        }
        StockMovementEntity entity = mapToEntity(stock_movementDto);
        stock_movementRepository.save(entity);
        return mapToCreateDto(entity);
    }

    public List<CreateStockMovement> saveAll(List<CreateStockMovement> stock_movementsDtos) {
        List<StockMovementEntity> entities = stock_movementsDtos.stream().map(this::mapToEntity).collect(Collectors.toList());

        List<StockMovementEntity> savedEntities = stock_movementRepository.saveAll(entities);

        return savedEntities.stream().map(this::mapToCreateDto).collect(Collectors.toList());
    }

    public UpdateStockMovement updateStockMovement(String id,UpdateStockMovement stock_movementDto) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Stock movement with ID " + id + " not found"));

        if(!furnitureRepository.existsById(stock_movementDto.getFurnitureId())) {
            throw new EntityNotFoundException("Furniture with ID " + stock_movementDto.getFurnitureId() + " not found");
        }

        if(stock_movementDto.getQuantity() != 0) {
            entity.setQuantity(stock_movementDto.getQuantity());
        }

        if(stock_movementDto.getTimestamp() != null) {
            entity.setTimestamp(stock_movementDto.getTimestamp());
        }

        entity = stock_movementRepository.save(entity);
        return mapToUpdateDto(entity);
    }

    public void deleteStockMovement(String id) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
                stock_movementRepository.delete(entity);
    }

    public void deleteAllStockMovements() {
        stock_movementRepository.deleteAll();
    }

    public CreateStockMovement updateStockMovement(String id, CreateStockMovement stock_movement) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        entity.setFurnitureId(stock_movement.getFurnitureId());
        entity.setQuantity(stock_movement.getQuantity());
        entity.setTimestamp(stock_movement.getTimestamp());
        stock_movementRepository.save(entity);
        return new CreateStockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp());
    }

    public List<CreateStockMovement> getStockMovementsByProductId(String productId) {
        return stock_movementRepository.findByFurnitureId(productId).stream()
                .map(entity -> new CreateStockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<CreateStockMovement> getStockMovementsByTimestamp(Date timestamp) {
        return stock_movementRepository.findByTimestamp(timestamp).stream()
                .map(entity -> new CreateStockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    private CreateStockMovement mapToCreateDto(StockMovementEntity entity) {
        return new CreateStockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp());
    }

    private UpdateStockMovement mapToUpdateDto(StockMovementEntity entity) {
        return new UpdateStockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp());
    }

    private StockMovementEntity mapToEntity(CreateStockMovement dto) {
        return new StockMovementEntity(dto.getFurnitureId(), dto.getQuantity(), dto.getTimestamp());
    }

}
