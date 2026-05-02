package com.oct.octquiz.Model.Categoria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void save_shouldAssignIdAndSave() {
        // Arrange
        CategoriaEntity category = new CategoriaEntity();
        category.setNome("History");
        
        when(categoriaRepository.findMaxID()).thenReturn(5);

        // Act
        categoryService.save(category);

        // Assert
        assertEquals(6, category.getId());
        verify(categoriaRepository).save(category);
        verify(categoriaRepository).flush();
    }

    @Test
    void save_shouldAssignIdOneIfNoCategoriesExist() {
        // Arrange
        CategoriaEntity category = new CategoriaEntity();
        category.setNome("Science");

        when(categoriaRepository.findMaxID()).thenReturn(null);

        // Act
        categoryService.save(category);

        // Assert
        assertEquals(1, category.getId());
        verify(categoriaRepository).save(category);
        verify(categoriaRepository).flush();
    }
}
