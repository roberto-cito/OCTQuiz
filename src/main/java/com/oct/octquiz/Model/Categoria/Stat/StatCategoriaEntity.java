package com.oct.octquiz.Model.Categoria.Stat;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import jakarta.persistence.*;

@Entity
@Table(name="stat_categoria",schema="octquiz")
@IdClass(StatCategoriaID.class)
public class StatCategoriaEntity {
    @Id
    @GeneratedValue(generator = "stat_categoria_seq")
    private int id;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaEntity categoria;

    @Column(length = 10000)
    private String stat;

    public StatCategoriaEntity() {}

    public StatCategoriaEntity(CategoriaEntity categoria, String stat) {
        this.categoria = categoria;
        this.stat = stat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
