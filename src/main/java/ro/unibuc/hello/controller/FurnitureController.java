package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.CreateFurniture;
import ro.unibuc.hello.dto.UpdateFurniture;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.service.FurnitureService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/furniture")
public class FurnitureController {
    
    @Autowired
    private FurnitureService furnitureService;

    @GetMapping("/{sku}")
    public CreateFurniture getFurnitureBySku(@PathVariable String sku) throws EntityNotFoundException {
        return furnitureService.getFurnitureBySku(sku);
    }

    @GetMapping
    public List<CreateFurniture> getAllFurniture() {
        return furnitureService.getAllFurniture();
    }

    @PostMapping
    public ResponseEntity<?> createFurniture(@Valid @RequestBody CreateFurniture furniture, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        CreateFurniture savedFurniture = furnitureService.saveFurniture(furniture);
        return new ResponseEntity<>(savedFurniture, HttpStatus.CREATED);
    }

    @PutMapping("/{sku}")
    public ResponseEntity<?> updateFurniture(@PathVariable String sku, @Valid @RequestBody UpdateFurniture furniture, BindingResult result) throws EntityNotFoundException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        UpdateFurniture updatedFurniture = furnitureService.updateFurniture(sku, furniture);
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
