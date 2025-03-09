package ro.unibuc.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.inventory_management.data.CategoryEntity;
import ro.unibuc.inventory_management.data.CategoryRepository;
import ro.unibuc.inventory_management.dto.Category;
import ro.unibuc.inventory_management.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryByCode(int code) throws EntityNotFoundException {
        Optional<CategoryEntity> optionalEntity = categoryRepository.findByCode(code);
        CategoryEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Category with code " + code + " not found"));
        return new Category(entity.getCategoryCode(), entity.getName());
    }

    public List<Category> getAllCategories() {
        List<CategoryEntity> entities = categoryRepository.findAll();
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Category saveCategory(Category categoryDto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryCode(categoryDto.getCategoryCode());
        entity.setName(categoryDto.getName());
        categoryRepository.save(entity);
        return new Category(entity.getCategoryCode(), entity.getName());
    }

    public List<Category> saveAllCategories(List<Category> categoryDtos) {
        List<CategoryEntity> entities = categoryDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<CategoryEntity> savedEntities = categoryRepository.saveAll(entities);

        return savedEntities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Category updateCategory(int code, Category categoryDto) throws EntityNotFoundException {
        CategoryEntity entity = categoryRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Category with code " + code + " not found"));
        entity.setName(categoryDto.getName());
        entity = categoryRepository.save(entity);
        return new Category(entity.getCategoryCode(), entity.getName());
    }

    public void deleteCategory(int code) throws EntityNotFoundException {
        CategoryEntity entity = categoryRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Category with code " + code + " not found"));
        categoryRepository.delete(entity);
    }

    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    private Category mapToDto(CategoryEntity entity) {
        return new Category(entity.getCategoryCode(), entity.getName());
    }

    private CategoryEntity mapToEntity(Category dto) {
        return new CategoryEntity(dto.getCategoryCode(), dto.getName());
    }
}
