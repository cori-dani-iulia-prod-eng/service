package main.java.ro.unibuc.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> createStockMovement(@Valid @RequestBody StockMovement stockMovement, BindingResult result) throws InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        StockMovement savedStockMovement = stockMovementService.saveStockMovement(stockMovement);
        return new ResponseEntity<>(savedStockMovement, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public List<StockMovement> saveAllStockMovements(@Valid @RequestBody List<StockMovement> stockMovements) throws InvalidInputException {
        return stockMovementService.saveAll(stockMovements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStockMovement(@PathVariable String id, @Valid @RequestBody StockMovement stockMovement, BindingResult result) throws EntityNotFoundException, InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        StockMovement updatedStockMovement = stockMovementService.updateStockMovement(id, stockMovement);
        return new ResponseEntity<>(updatedStockMovement, HttpStatus.OK);
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
