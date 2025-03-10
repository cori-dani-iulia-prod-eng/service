package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.Category;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.service.CategoryService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Get a category by its code
    @GetMapping("/{code}")
    public Category getCategoryByCode(@PathVariable int code) throws EntityNotFoundException {
        return categoryService.getCategoryByCode(code);
    }

    // Get all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category, BindingResult result) {
        if (result.hasErrors()) {
            // Handle validation errors and throw InvalidInputException
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        Category savedCategory = categoryService.saveCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Update an existing category by its code
    @PutMapping("/{code}")
    public ResponseEntity<?> updateCategory(@PathVariable int code, @Valid @RequestBody Category category, BindingResult result) throws EntityNotFoundException {
        if (result.hasErrors()) {
            // Handle validation errors and throw InvalidInputException
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }
        Category updatedCategory = categoryService.updateCategory(code, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // Delete a category by its code
    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteCategory(@PathVariable int code) throws EntityNotFoundException {
        categoryService.deleteCategory(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content on successful deletion
    }

    // Delete all categories (optional endpoint)
    @DeleteMapping
    public ResponseEntity<?> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content on successful deletion
    }
}
