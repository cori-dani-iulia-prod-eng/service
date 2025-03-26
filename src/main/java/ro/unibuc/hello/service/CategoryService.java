package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.hello.data.CategoryEntity;
import ro.unibuc.hello.data.CategoryRepository;
import ro.unibuc.hello.dto.CreateCategory;
import ro.unibuc.hello.dto.UpdateCategory;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get a category by its code
     * @param code the code of the category
     * @return the category with the given code
     */
    public CreateCategory getCategoryByCode(int code) throws EntityNotFoundException {
        Optional<CategoryEntity> optionalEntity = categoryRepository.findByCategoryCode(code);
        CategoryEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Category with code " + code + " not found"));
        return new CreateCategory(entity.getCategoryCode(), entity.getName());
    }

    /**
     * Get all categories
     * @return list of all categories
     */
    public List<CreateCategory> getAllCategories() {
        List<CategoryEntity> entities = categoryRepository.findAll();
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Create a new category
     * @param categoryDto the category to be created DTO
     * @return the created category
     */
    public CreateCategory saveCategory(CreateCategory categoryDto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryCode(categoryDto.getCategoryCode());
        entity.setName(categoryDto.getName());
        categoryRepository.save(entity);
        return new CreateCategory(entity.getCategoryCode(), entity.getName());
    }

    /**
     * Create a list of new categories
     * @param categoryDtos the list of categories to be created DTOs
     * @return the list of created categories
     */
    public List<CreateCategory> saveAllCategories(List<CreateCategory> categoryDtos) {
        List<CategoryEntity> entities = categoryDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<CategoryEntity> savedEntities = categoryRepository.saveAll(entities);

        return savedEntities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Update a category by its code
     * @param code the code of the category
     * @param categoryDto the category to be updated DTO
     * @return the updated category
     */
    public UpdateCategory updateCategory(int code, UpdateCategory categoryDto) throws EntityNotFoundException {
        CategoryEntity entity = categoryRepository.findByCategoryCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Category with code " + code + " not found"));

        if(categoryDto.getName() != null) {
            entity.setName(categoryDto.getName());
        }

        if(categoryDto.getCategoryCode() != 0) {
            entity.setCategoryCode(categoryDto.getCategoryCode());
        }

        entity = categoryRepository.save(entity);
        return new UpdateCategory(entity.getCategoryCode(), entity.getName());
    }

    /**
     * Delete a category by its code
     * @param code the code of the category
     */
    public void deleteCategory(int code) throws EntityNotFoundException {
        CategoryEntity entity = categoryRepository.findByCategoryCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Category with code " + code + " not found"));
        categoryRepository.delete(entity);
    }

    /**
     * Delete all categories
     */
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    /**
     * Map a category entity to a category DTO
     * @param entity the category entity
     * @return the category DTO
     */
    private CreateCategory mapToDto(CategoryEntity entity) {
        return new CreateCategory(entity.getCategoryCode(), entity.getName());
    }

    /**
     * Map a category DTO to a category entity
     * @param dto the category DTO
     * @return the category entity
     */
    private CategoryEntity mapToEntity(CreateCategory dto) {
        return new CategoryEntity(dto.getCategoryCode(), dto.getName());
    }
}
