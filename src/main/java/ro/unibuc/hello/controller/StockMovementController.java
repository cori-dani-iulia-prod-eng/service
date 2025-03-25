package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.hello.dto.CreateStockMovement;
import ro.unibuc.hello.dto.UpdateStockMovement;
import ro.unibuc.hello.service.StockMovementService;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stock_movements")
public class StockMovementController {

    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping
    public List<CreateStockMovement> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateStockMovement> getStockMovementById(@PathVariable String id) throws EntityNotFoundException {
        CreateStockMovement stockMovement = stockMovementService.getStockMovementById(id);
        return ResponseEntity.ok(stockMovement);
    }

    @PostMapping
    public ResponseEntity<?> createStockMovement(@Valid @RequestBody CreateStockMovement stockMovement, BindingResult result) throws InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        CreateStockMovement savedStockMovement = stockMovementService.saveStockMovement(stockMovement);
        return new ResponseEntity<>(savedStockMovement, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public List<CreateStockMovement> saveAllStockMovements(@Valid @RequestBody List<CreateStockMovement> stockMovements) throws InvalidInputException {
        return stockMovementService.saveAll(stockMovements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStockMovement(@PathVariable String id, @Valid @RequestBody UpdateStockMovement stockMovement, BindingResult result) throws EntityNotFoundException, InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        UpdateStockMovement updatedStockMovement = stockMovementService.updateStockMovement(id, stockMovement);
        return new ResponseEntity<>(updatedStockMovement, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable String id) throws EntityNotFoundException {
        stockMovementService.deleteStockMovement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllStockMovements() {
        stockMovementService.deleteAllStockMovements();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
