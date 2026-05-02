package com.oct.octquiz.Model.Categoria.Stat;

import java.util.Objects;

public class StatCategoriaID {
    private int id;
    private int categoria;

    public StatCategoriaID() {}

    public StatCategoriaID(int id, int categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StatCategoriaID that = (StatCategoriaID) o;
        return id == that.id && categoria == that.categoria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoria);
    }
}
