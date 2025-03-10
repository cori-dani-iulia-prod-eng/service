package main.java.ro.unibuc.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.inventory_management.dto.StockMovement;
import ro.unibuc.inventory_management.service.StockMovementService;
import ro.unibuc.inventory_management.exception.EntityNotFoundException;
import ro.unibuc.inventory_management.exception.InvalidInputException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stock_movements")
public class StockMovementController {

    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping
    public List<StockMovement> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getStockMovementById(@PathVariable String id) throws EntityNotFoundException {
        StockMovement stockMovement = stockMovementService.getStockMovementById(id);
        return ResponseEntity.ok(stockMovement);
    }

    @PostMapping
    public StockMovement saveStockMovement(@Valid @RequestBody StockMovement stockMovement) throws InvalidInputException {
        return stockMovementService.saveStockMovement(stockMovement);
    }

    @PostMapping("/batch")
    public List<StockMovement> saveAllStockMovements(@Valid @RequestBody List<StockMovement> stockMovements) throws InvalidInputException {
        return stockMovementService.saveAll(stockMovements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovement> updateStockMovement(@PathVariable String id, @Valid @RequestBody StockMovement stockMovement) throws EntityNotFoundException, InputValidationException {
        StockMovement updatedStockMovement = stockMovementService.updateStockMovement(id, stockMovement);
        return ResponseEntity.ok(updatedStockMovement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable String id) throws EntityNotFoundException {
        stockMovementService.deleteStockMovement(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public void deleteAllStockMovements() {
        stockMovementService.deleteAllStockMovements();
    }
}
