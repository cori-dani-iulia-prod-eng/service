package main.java.ro.unibuc.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.java.ro.unibuc.inventory_management.data.SupplierEntity;
import main.java.ro.unibuc.inventory_management.data.SupplierRepository;
import ro.unibuc.inventory_management.data.StockMovementRepository;
import ro.unibuc.inventory_management.data.StockMovementEntity;
import ro.unibuc.inventory_management.dto.StockMovement;
import ro.unibuc.inventory_management.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class StockMovementService {
        @Autowired
    private StockMovementRepository stock_movementRepository;

    public List<StockMovement> getAllStockMovements() {
        return stock_movementRepository.findAll().stream()
                .map(entity -> new StockMovement(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getMovementType(), entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    public StockMovement getStockMovementById(String id) throws EntityNotFoundException {
        StockMovementEntity entity = stock_movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return new StockMovement(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getMovementType(), entity.getTimestamp());
    }

    public StockMovement saveStockMovement(StockMovement stock_movement) {
        StockMovementEntity entity = new StockMovementEntity();
        entity.setId(stock_movement.getId());
        entity.setProductId(stock_movement.getProductId());
        entity.setQuantity(stock_movement.getQuantity());
        entity.setMovementType(stock_movement.getMovementType());
        entity.setTimestamp(stock_movement.getTimestamp());
        stock_movementRepository.save(entity);
        return new StockMovement(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getMovementType(), entity.getTimestamp());
    }

    public List<StockMovement> saveAll(List<StockMovement> stock_movements) {
        List<StockMovementEntity> entities = stock_movements.stream()
                .map(stock_movement -> {
                    StockMovementEntity entity = new StockMovementEntity();
                    entity.setId(stock_movement.getId());
                    entity.setProductId(stock_movement.getProductId());
                    entity.setQuantity(stock_movement.getQuantity());
                    entity.setMovementType(stock_movement.getMovementType());
                    entity.setTimestamp(stock_movement.getTimestamp());
                    return entity;
                })
                .collect(Collectors.toList());

        List<StockMovementEntity> savedEntities = stock_movementRepository.saveAll(entities);
        return savedEntities.stream()
                .map(entity -> new StockMovement(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getMovementType(), entity.getTimestamp()))
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
        entity.setProductId(stock_movement.getProductId());
        entity.setQuantity(stock_movement.getQuantity());
        entity.setMovementType(stock_movement.getMovementType());
        entity.setTimestamp(stock_movement.getTimestamp());
        stock_movementRepository.save(entity);
        return new StockMovement(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getMovementType(), entity.getTimestamp());
    }

    public List<StockMovement> getStockMovementsByProductId(String productId) {
        return stock_movementRepository.findByProductId(productId).stream()
                .map(entity -> new StockMovement(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getMovementType(), entity.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<StockMovement> getStockMovementsByTimestamp(String timestamp) {
        return stock_movementRepository.findByTimestamp(timestamp).stream()
                .map(entity -> new StockMovement(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getMovementType(), entity.getTimestamp()))
                .collect(Collectors.toList());
    }

}
