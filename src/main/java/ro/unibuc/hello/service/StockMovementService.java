package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import ro.unibuc.hello.data.StockMovementRepository;
import ro.unibuc.hello.data.StockMovementEntity;
import ro.unibuc.hello.dto.StockMovement;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

@Component
public class StockMovementService {
        @Autowired
    private StockMovementRepository stock_movementRepository;

    public List<StockMovement> getAllStockMovements() {
        return stock_movementRepository.findAll().stream()
                .map(entity -> new StockMovement(entity.getFurnitureId(), entity.getQuantity(),  entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    public StockMovement getStockMovementById(String id) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return new StockMovement(entity.getFurnitureId(), entity.getQuantity(),  entity.getTimestamp());
    }

    public StockMovement saveStockMovement(StockMovement stock_movement) {
        StockMovementEntity entity = new StockMovementEntity();
        entity.setFurnitureId(stock_movement.getFurnitureId());
        entity.setQuantity(stock_movement.getQuantity());
        entity.setTimestamp(stock_movement.getTimestamp());
        stock_movementRepository.save(entity);
        return new StockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp());
    }

    public List<StockMovement> saveAll(List<StockMovement> stock_movements) {
        List<StockMovementEntity> entities = stock_movements.stream()
                .map(stock_movement -> {
                    StockMovementEntity entity = new StockMovementEntity();
                    entity.setFurnitureId(stock_movement.getFurnitureId());
                    entity.setQuantity(stock_movement.getQuantity());
                    entity.setTimestamp(stock_movement.getTimestamp());
                    return entity;
                })
                .collect(Collectors.toList());

        List<StockMovementEntity> savedEntities = stock_movementRepository.saveAll(entities);
        return savedEntities.stream()
                .map(entity -> new StockMovement(entity.getFurnitureId(), entity.getQuantity(),  entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    public void deleteStockMovement(String id) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
                stock_movementRepository.delete(entity);
    }

    public void deleteAllStockMovements() {
        stock_movementRepository.deleteAll();
    }

    public StockMovement updateStockMovement(String id, StockMovement stock_movement) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        entity.setFurnitureId(stock_movement.getFurnitureId());
        entity.setQuantity(stock_movement.getQuantity());
        entity.setTimestamp(stock_movement.getTimestamp());
        stock_movementRepository.save(entity);
        return new StockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp());
    }

    public List<StockMovement> getStockMovementsByProductId(String productId) {
        return stock_movementRepository.findByFurnitureId(productId).stream()
                .map(entity -> new StockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<StockMovement> getStockMovementsByTimestamp(Date timestamp) {
        return stock_movementRepository.findByTimestamp(timestamp).stream()
                .map(entity -> new StockMovement(entity.getFurnitureId(), entity.getQuantity(), entity.getTimestamp()))
                .collect(Collectors.toList());
    }

}
