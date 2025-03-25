package ro.unibuc.hello.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.unibuc.hello.data.CategoryEntity;
import ro.unibuc.hello.data.CategoryRepository;
import ro.unibuc.hello.dto.CreateCategory;
import ro.unibuc.hello.dto.UpdateCategory;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService = new CategoryService();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCategoryByCode() {
        // Arrange
        int code = 1;
        when(categoryRepository.findByCategoryCode(code)).thenReturn(Optional.of(new CategoryEntity(code, "Category")));

        // Act
        CreateCategory category = categoryService.getCategoryByCode(code);

        // Assert
        assertNotNull(category);
        assertEquals(code, category.getCategoryCode());
    }

    @Test
    void testGetCategoryByCode_NotFound() {
        // Arrange
        int code = 1;
        when(categoryRepository.findByCategoryCode(code)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByCode(code));
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(new CategoryEntity(1, "Category 1"), new CategoryEntity(2, "Category 2")));

        // Act
        List<CreateCategory> categories = categoryService.getAllCategories();

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals(1, categories.get(0).getCategoryCode());
        assertEquals("Category 1", categories.get(0).getName());
        assertEquals(2, categories.get(1).getCategoryCode());
        assertEquals("Category 2", categories.get(1).getName());
    }

    @Test
    void testSaveCategory() {
        // Arrange
        CreateCategory categoryDto = new CreateCategory(1, "Category");
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity(1, "Category"));

        // Act
        CreateCategory category = categoryService.saveCategory(categoryDto);

        // Assert
        assertNotNull(category);
        assertEquals(categoryDto.getCategoryCode(), category.getCategoryCode());
        assertEquals(categoryDto.getName(), category.getName());
    }

    @Test
    void testSaveAllCategories() {
        // Arrange
        CreateCategory categoryDto1 = new CreateCategory(1, "Category 1");
        CreateCategory categoryDto2 = new CreateCategory(2, "Category 2");
        when(categoryRepository.saveAll(anyList())).thenReturn(List.of(new CategoryEntity(1, "Category 1"), new CategoryEntity(2, "Category 2")));

        // Act
        List<CreateCategory> categories = categoryService.saveAllCategories(List.of(categoryDto1, categoryDto2));

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals(categoryDto1.getCategoryCode(), categories.get(0).getCategoryCode());
        assertEquals(categoryDto1.getName(), categories.get(0).getName());
        assertEquals(categoryDto2.getCategoryCode(), categories.get(1).getCategoryCode());
        assertEquals(categoryDto2.getName(), categories.get(1).getName());
    }

    @Test
    void testUpdateCategory_ExistingEntity() throws EntityNotFoundException {
        // Arrange
        int code = 1;
        UpdateCategory categoryDto = new UpdateCategory(1, "Updated Category");
        when(categoryRepository.findByCategoryCode(code)).thenReturn(Optional.of(new CategoryEntity(code, "Category")));
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity(code, "Updated Category"));

        // Act
        UpdateCategory category = categoryService.updateCategory(code, categoryDto);

        // Assert
        assertNotNull(category);
        assertEquals(code, category.getCategoryCode());
        assertEquals("Updated Category", category.getName());
    }

    @Test
    void testUpdateCategory_NonExistingEntity() {
        // Arrange
        int code = 1;
        UpdateCategory categoryDto = new UpdateCategory(code, "Updated Category");
        when(categoryRepository.findByCategoryCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.updateCategory(code, categoryDto));
    }

    @Test
    void testDeleteCategory_ExistingEntity() throws EntityNotFoundException {
        // Arrange
        int code = 1;
        CategoryEntity entity = new CategoryEntity(code, "Category to delete");
        when(categoryRepository.findByCategoryCode(code)).thenReturn(Optional.of(entity));

        // Act
        categoryService.deleteCategory(code);

        // Assert
        verify(categoryRepository, times(1)).delete(entity);
    }

    @Test
    void testDeleteCategory_NonExistingEntity() {
        // Arrange
        int code = 1;
        when(categoryRepository.findByCategoryCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(code));
    }

    @Test
    void testDeleteAllCategories() {
        // Act
        categoryService.deleteAllCategories();

        // Assert
        verify(categoryRepository, times(1)).deleteAll();
    }

}