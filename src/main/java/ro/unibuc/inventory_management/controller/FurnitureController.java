package ro.unibuc.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.inventory_management.dto.Furniture;
import ro.unibuc.inventory_management.exception.EntityNotFoundException;
import ro.unibuc.inventory_management.exception.InvalidInputException;
import ro.unibuc.inventory_management.service.FurnitureService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/furniture")
public class FurnitureController {
    
    @Autowired
    private FurnitureService furnitureService;

    @GetMapping("/{sku}")
    public Furniture getFurnitureBySku(@PathVariable String sku) throws EntityNotFoundException {
        return furnitureService.getFurnitureBySku(sku);
    }

    @GetMapping
    public List<Furniture> getAllFurniture() {
        return furnitureService.getAllFurniture();
    }

    @PostMapping
    public ResponseEntity<?> createFurniture(@Valid @RequestBody Furniture furniture, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        Furniture savedFurniture = furnitureService.saveFurniture(furniture);
        return new ResponseEntity<>(savedFurniture, HttpStatus.CREATED);
    }

    @PutMapping("/{sku}")
    public ResponseEntity<?> updateFurniture(@PathVariable String sku, @Valid @RequestBody Furniture furniture, BindingResult result) throws EntityNotFoundException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        Furniture updatedFurniture = furnitureService.updateFurniture(sku, furniture);
        return new ResponseEntity<>(updatedFurniture, HttpStatus.OK);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<?> deleteFurniture(@PathVariable String sku) throws EntityNotFoundException {
        furnitureService.deleteFurniture(sku);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllFurniture() {
        furnitureService.deleteAllFurniture();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
