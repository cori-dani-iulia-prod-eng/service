package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.hello.dto.CreateCategory;
import ro.unibuc.hello.dto.CreateSupplier;
import ro.unibuc.hello.dto.UpdateSupplier;
import ro.unibuc.hello.service.SupplierService;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public List<CreateSupplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{name}")
    public ResponseEntity<CreateSupplier> getSupplierByName(@PathVariable String name) throws EntityNotFoundException {
        CreateSupplier supplier = supplierService.getSupplierByName(name);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody CreateSupplier createSupplier, BindingResult result) throws InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        CreateSupplier savedSupplier = supplierService.saveSupplier(createSupplier);
        return new ResponseEntity<>(savedSupplier, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public List<CreateSupplier> saveAllSuppliers(@Valid @RequestBody List<CreateSupplier> createSuppliers) throws InvalidInputException {
        return supplierService.saveAll(createSuppliers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable String id, @Valid @RequestBody UpdateSupplier updateSupplier, BindingResult result) throws EntityNotFoundException, InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }

        UpdateSupplier updatedSupplier = supplierService.updateSupplier(id, updateSupplier);
        return new ResponseEntity<>(updatedSupplier, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable String id) throws EntityNotFoundException {
        supplierService.deleteSupplier(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllSuppliers() {
        supplierService.deleteAllSuppliers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/product-counts")
    public Map<String, Map<String, Long>> getProductCountsPerSupplier() {
        Map<String, Map<String, Long>> productCounts = supplierService.getFurnitureCountByCategoryAndSupplier();

        if (productCounts.isEmpty()) {
            throw new EntityNotFoundException("No product counts found for any supplier");
        }

        return productCounts;
    }
}
