package com.oct.octquiz.Model.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="categorie",schema="octquiz")
public class CategoriaEntity {
    @Id
    @Column(name="id",nullable = false,unique = true)
    private int id;

    @Column(name="nome",nullable=false)
    private String nome;

    @Column(name="tempo",nullable = false)
    private int tempo;

    @Column(name="visibile", nullable = false)
    private boolean visibile = true;

    public CategoriaEntity() {}

    public CategoriaEntity(String nome, int tempo, boolean visibile) {
        this.id = 0;
        this.nome = nome;
        this.tempo = tempo;
        this.visibile = visibile;
    }

    public CategoriaEntity(int id, String nome, int tempo, boolean visibile) {
        this.id = id;
        this.nome = nome;
        this.tempo = tempo;
        this.visibile = visibile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMin() {
        return tempo;
    }

    public int getHour() {
        return tempo/60;
    }

    public int getRemainMinute() {
        return tempo%60;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}
