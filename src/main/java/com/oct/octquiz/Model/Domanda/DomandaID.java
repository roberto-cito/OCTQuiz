package com.oct.octquiz.Model.Domanda;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;

import java.io.Serializable;
import java.util.Objects;

public class DomandaID implements Serializable {
    private int id;
    private CategoriaEntity categoria;

    public DomandaID() {
    }

    public DomandaID(int id, CategoriaEntity categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        DomandaID domandaID = (DomandaID) object;
        return id == domandaID.id && Objects.equals(categoria, domandaID.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoria);
    }
}
