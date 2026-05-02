package com.oct.octquiz.Model.Domanda;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DomandaServiceTest {

    @Mock
    private DomandaRepository domandaRepository;

    @InjectMocks
    private DomandaService domandaService;

    @Test
    void save_shouldAssignIdAndSave() {
        // Arrange
        CategoriaEntity category = new CategoriaEntity();
        DomandaEntity domanda = new DomandaEntity();
        domanda.setCategoria(category);
        domanda.setDomanda("What is 2+2?");

        when(domandaRepository.findMaxIdByCategoria(category)).thenReturn(5);

        // Act
        domandaService.save(domanda);

        // Assert
        assertEquals(6, domanda.getId());
        verify(domandaRepository).save(domanda);
        verify(domandaRepository).flush();
    }

    @Test
    void save_shouldAssignIdOneIfNoQuestionsExist() {
        // Arrange
        CategoriaEntity category = new CategoriaEntity();
        DomandaEntity domanda = new DomandaEntity();
        domanda.setCategoria(category);
        domanda.setDomanda("What is 2+2?");

        when(domandaRepository.findMaxIdByCategoria(category)).thenReturn(null);

        // Act
        domandaService.save(domanda);

        // Assert
        assertEquals(1, domanda.getId());
        verify(domandaRepository).save(domanda);
        verify(domandaRepository).flush();
    }

    @Test
    void update_shouldSaveUpdatedQuestion() {
        // Arrange
        DomandaEntity domanda = new DomandaEntity();
        domanda.setDomanda("Original Question");

        // Act
        domanda.setDomanda("Updated Question");
        domandaService.update(domanda);

        // Assert
        verify(domandaRepository).save(domanda);
    }

    @Test
    void findByCategoriaAndId_shouldReturnQuestion() {
        // Arrange
        CategoriaEntity category = new CategoriaEntity();
        DomandaEntity domanda = new DomandaEntity();
        domanda.setCategoria(category);
        domanda.setId(1);
        
        when(domandaRepository.findByCategoriaAndId(category, 1)).thenReturn(Optional.of(domanda));

        // Act
        DomandaEntity result = domandaService.findByCategoriaAndId(category, 1);

        // Assert
        assertEquals(domanda, result);
    }
}
