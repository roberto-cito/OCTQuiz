package com.oct.octquiz.Model.Categoria.Stat;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StatCategoriaEntityTest {

    @Test
    void testConstructorAndGetters() {
        CategoriaEntity mockCategory = mock(CategoriaEntity.class);
        String stat = "Test Stat";
        StatCategoriaEntity entity = new StatCategoriaEntity(mockCategory, stat);

        assertEquals(mockCategory, entity.getCategoria());
        assertEquals(stat, entity.getStat());
    }

    @Test
    void testSetters() {
        StatCategoriaEntity entity = new StatCategoriaEntity();
        CategoriaEntity mockCategory = mock(CategoriaEntity.class);
        String stat = "New Stat";
        int id = 100;

        entity.setId(id);
        entity.setCategoria(mockCategory);
        entity.setStat(stat);

        assertEquals(id, entity.getId());
        assertEquals(mockCategory, entity.getCategoria());
        assertEquals(stat, entity.getStat());
    }
}
