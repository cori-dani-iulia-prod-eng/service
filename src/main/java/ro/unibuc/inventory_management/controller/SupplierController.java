package main.java.ro.unibuc.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.inventory_management.dto.Supplier;
import ro.unibuc.inventory_management.service.SupplierService;
import ro.unibuc.inventory_management.exception.EntityNotFoundException;
import ro.unibuc.inventory_management.exception.InvalidInputException;

import javax.validation.Valid;
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

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable String id) throws EntityNotFoundException {
        Supplier supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public Supplier saveSupplier(@Valid @RequestBody Supplier supplier) throws InvalidInputException {
        return supplierService.saveSupplier(supplier);
    }

    @PostMapping("/batch")
    public List<Supplier> saveAllSuppliers(@Valid @RequestBody List<Supplier> suppliers) throws InvalidInputException {
        return supplierService.saveAll(suppliers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable String id, @Valid @RequestBody Supplier supplier) throws EntityNotFoundException, InputValidationException {
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
