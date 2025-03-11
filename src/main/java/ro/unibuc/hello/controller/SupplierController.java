package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ro.unibuc.hello.dto.Supplier;
import ro.unibuc.hello.service.SupplierService;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Supplier> getSupplierByName(@PathVariable String name) throws EntityNotFoundException {
        Supplier supplier = supplierService.getSupplierByName(name);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody Supplier supplier, BindingResult result) throws InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        Supplier savedSupplier = supplierService.saveSupplier(supplier);
        return new ResponseEntity<>(savedSupplier, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public List<Supplier> saveAllSuppliers(@Valid @RequestBody List<Supplier> suppliers) throws InvalidInputException {
        return supplierService.saveAll(suppliers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable String id, @Valid @RequestBody Supplier supplier, BindingResult result) throws EntityNotFoundException, InvalidInputException {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }

        Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String id) throws EntityNotFoundException {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public void deleteAllSuppliers() {
        supplierService.deleteAllSuppliers();
    }
}
