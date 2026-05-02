package com.oct.octquiz.Model.Categoria.Stat;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StatCategoriaServiceTest {

    @Mock
    private StatCategoriaRepository statCategoriaRepository;

    @InjectMocks
    private StatCategoriaService statCategoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        StatCategoriaEntity entity = new StatCategoriaEntity();
        statCategoriaService.save(entity);
        verify(statCategoriaRepository, times(1)).save(entity);
    }

    @Test
    void testDeleteAllByCategoria() {
        CategoriaEntity categoria = new CategoriaEntity();
        statCategoriaService.deleteAllByCategoria(categoria);
        verify(statCategoriaRepository, times(1)).deleteAllByCategoria(categoria);
    }

    @Test
    void testGetStatByCategoria() {
        CategoriaEntity categoria = new CategoriaEntity();
        StatCategoriaEntity stat1 = new StatCategoriaEntity(categoria, "Stat1");
        StatCategoriaEntity stat2 = new StatCategoriaEntity(categoria, "Stat2");

        when(statCategoriaRepository.findAllByCategoria(categoria)).thenReturn(Arrays.asList(stat1, stat2));

        List<String> results = statCategoriaService.getStatByCategoria(categoria);

        assertEquals(2, results.size());
        assertEquals("Stat1", results.get(0));
        assertEquals("Stat2", results.get(1));
    }

    @Test
    void testGetStatByCategoriaEmpty() {
        CategoriaEntity categoria = new CategoriaEntity();
        when(statCategoriaRepository.findAllByCategoria(categoria)).thenReturn(Collections.emptyList());

        List<String> results = statCategoriaService.getStatByCategoria(categoria);

        assertEquals(0, results.size());
    }
}
