package com.oct.octquiz.Model.Support;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="support",schema="octquiz")
public class SupportEntity {
    @Id
    @GeneratedValue(generator = "support_seq")
    private int id;

    @Column(name="email",nullable=false)
    private String email;

    @Column(name="categoria",nullable=false)
    private String categoria;

    @Column(name="messaggio",nullable=false)
    private String messaggio;

    @Column(name="data",nullable=false)
    private Date data;

    public SupportEntity() {
    }

    public SupportEntity(String email, String categoria, String messaggio, Date data) {
        this.email = email;
        this.categoria = categoria;
        this.messaggio = messaggio;
        this.data = data;
    }

    public SupportEntity(int id, String email, String categoria, String messaggio, Date data) {
        this.id = id;
        this.email = email;
        this.categoria = categoria;
        this.messaggio = messaggio;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
