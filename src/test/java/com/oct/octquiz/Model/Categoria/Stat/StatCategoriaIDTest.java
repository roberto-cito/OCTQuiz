package com.oct.octquiz.Model.Categoria.Stat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatCategoriaIDTest {

    @Test
    void testEquals() {
        StatCategoriaID id1 = new StatCategoriaID(1, 10);
        StatCategoriaID id2 = new StatCategoriaID(1, 10);
        StatCategoriaID id3 = new StatCategoriaID(2, 10);
        StatCategoriaID id4 = new StatCategoriaID(1, 20);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(id1, id4);
        assertNotEquals(id1, null);
        assertNotEquals(id1, new Object());
    }

    @Test
    void testHashCode() {
        StatCategoriaID id1 = new StatCategoriaID(1, 10);
        StatCategoriaID id2 = new StatCategoriaID(1, 10);

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        StatCategoriaID id = new StatCategoriaID();
        id.setId(5);
        id.setCategoria(15);

        assertEquals(5, id.getId());
        assertEquals(15, id.getCategoria());
    }
}
