package com.oct.octquiz.Model.Domanda;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import jakarta.persistence.*;

@Entity
@Table(name="domande",schema="octquiz")
@IdClass(DomandaID.class)
public class DomandaEntity {
    @Id
    @Column(name="id",nullable=false)
    private int id;

    @Id
    @ManyToOne
    @JoinColumn(
            name = "id_categoria",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_domanda_categoria")
    )
    private CategoriaEntity categoria;

    @Column(name="domanda",nullable=false, columnDefinition = "TEXT")
    private String domanda;

    @Column(name="foto")
    private String foto;

    @Column(name="audio")
    private String audio;

    @Column(name="risposta1",nullable=false, columnDefinition = "TEXT")
    private String risposta1;

    @Column(name="risposta2",nullable=false, columnDefinition = "TEXT")
    private String risposta2;

    @Column(name="risposta3",nullable=false, columnDefinition = "TEXT")
    private String risposta3;

    @Column(name="risposta4",nullable=false, columnDefinition = "TEXT")
    private String risposta4;

    @Column(name="risposta_corretta",nullable=false)
    private int rispostaCorretta;

    @Column(name="onlytext",nullable=false)
    private boolean onlyText;

    public DomandaEntity() {
    }

    public DomandaEntity(CategoriaEntity categoria, String domanda) {
        this.categoria = categoria;
        this.domanda = domanda;
        this.foto = "";
        this.audio = "";
        this.risposta1 = "";
        this.risposta2 = "";
        this.risposta3 = "";
        this.risposta4 = "";
        this.rispostaCorretta = 0;
        this.onlyText = true;
    }

    public DomandaEntity(CategoriaEntity categoria, String domanda, String foto, String audio, String risposta1, String risposta2, String risposta3, String risposta4, int rispostaCorretta) {
        this.categoria = categoria;
        this.domanda = domanda;
        this.foto = foto;
        this.audio = audio;
        this.risposta1 = risposta1;
        this.risposta2 = risposta2;
        this.risposta3 = risposta3;
        this.risposta4 = risposta4;
        this.rispostaCorretta = rispostaCorretta;
        this.onlyText = false;
    }

    public DomandaEntity(int id, CategoriaEntity categoria, String domanda, String foto, String audio, String risposta1, String risposta2, String risposta3, String risposta4, int rispostaCorretta) {
        this.id = id;
        this.categoria = categoria;
        this.domanda = domanda;
        this.foto = foto;
        this.audio = audio;
        this.risposta1 = risposta1;
        this.risposta2 = risposta2;
        this.risposta3 = risposta3;
        this.risposta4 = risposta4;
        this.rispostaCorretta = rispostaCorretta;
        this.onlyText = false;
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

    public String getDomanda() {
        return domanda;
    }

    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getRisposta1() {
        return risposta1;
    }

    public void setRisposta1(String risposta1) {
        this.risposta1 = risposta1;
    }

    public String getRisposta2() {
        return risposta2;
    }

    public void setRisposta2(String risposta2) {
        this.risposta2 = risposta2;
    }

    public String getRisposta3() {
        return risposta3;
    }

    public void setRisposta3(String risposta3) {
        this.risposta3 = risposta3;
    }

    public String getRisposta4() {
        return risposta4;
    }

    public void setRisposta4(String risposta4) {
        this.risposta4 = risposta4;
    }

    public int getRispostaCorretta() {
        return rispostaCorretta;
    }

    public void setRispostaCorretta(int rispostaCorretta) {
        this.rispostaCorretta = rispostaCorretta;
    }

    public boolean isOnlyText() {
        return onlyText;
    }

    public void setOnlyText(boolean onlyText) {
        this.onlyText = onlyText;
    }

    @Override
    public String toString() {
        return "DomandaEntity{" +
                "id=" + id +
                ", categoria=" + categoria +
                ", domanda='" + domanda + '\'' +
                ", foto='" + foto + '\'' +
                ", audio='" + audio + '\'' +
                ", risposta1='" + risposta1 + '\'' +
                ", risposta2='" + risposta2 + '\'' +
                ", risposta3='" + risposta3 + '\'' +
                ", risposta4='" + risposta4 + '\'' +
                ", rispostaCorretta=" + rispostaCorretta +
                ", onlyText=" + onlyText +
                '}';
    }
}
