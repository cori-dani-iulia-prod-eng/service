package ro.unibuc.hello.controller;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class FurnitureController {
    
    @Autowired
    private FurnitureService furnitureService;

    /**
     * Endpoint to get furniture by SKU
     * @param sku the SKU of the furniture
     * @return the furniture with the given SKU
     * @throws EntityNotFoundException
     */
    @GetMapping("/{sku}")
    public CreateFurniture getFurnitureBySku(@PathVariable String sku) throws EntityNotFoundException {
        return furnitureService.getFurnitureBySku(sku);
    }

    /**
     * Endpoint to get all furniture
     * @return list of all furniture
     */
    @GetMapping
    public List<CreateFurniture> getAllFurniture() {
        return furnitureService.getAllFurniture();
    }

    /**
     * Endpoint to create a new furniture
     * @param furniture the furniture to be created
     * @param result   the result of the validation
     * @return the created furniture
     */
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

    /**
     * Endpoint to update furniture
     * @param sku the SKU of the furniture to be updated
     * @param furniture the response entity
     * @param result        the result of the validation
     * @return the updated furniture
     */
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

    /**
     * Endpoint to delete furniture by SKU
     * @param sku the SKU of the furniture to be deleted
     * @return response entity
     */
    @DeleteMapping("/{sku}")
    public ResponseEntity<?> deleteFurniture(@PathVariable String sku) throws EntityNotFoundException {
        furnitureService.deleteFurniture(sku);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to delete all furniture
     * @return response entity
     */
    @DeleteMapping
    public ResponseEntity<?> deleteAllFurniture() {
        furnitureService.deleteAllFurniture();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to get furniture with low stock
     * @param stockThreshold the threshold for low stock (default is 5)
     * @return list of furniture with stock less than the threshold
     */
    @GetMapping("/low-stock")
    public List<CreateFurniture> getLowStockFurniture(@RequestParam (defaultValue = "5") @Min(1) int stockThreshold) {
        int totalLowStockFurniture = furnitureService.getTotalLowStockFurniture(stockThreshold);

        if (totalLowStockFurniture == 0) {
            throw new EntityNotFoundException("No low stock furniture");
        } else {
            return furnitureService.getLowStockFurniture(stockThreshold);
        }
    }
}
