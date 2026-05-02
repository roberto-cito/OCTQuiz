package com.oct.octquiz.Model.User;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="USERS",schema="octquiz")
public class UserEntity {
    @Id
    @Column(name="email",nullable=false,unique=true)
    private String email;

    @Column(name="nome",nullable=false)
    private String nome;

    @Column(name="cognome",nullable=false)
    private String cognome;

    @Column(name="hash_password",nullable=false)
    private String hash_password;

    @Column(name="ruolo",nullable=false)
    private String ruolo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="userhascompleted",
            joinColumns = @JoinColumn(name="\"user\"",referencedColumnName="email"),
            inverseJoinColumns = @JoinColumn(name="categoria",referencedColumnName="id")

    )
    private Set<CategoriaEntity> categorie=new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String email, String nome, String cognome, String hash_password, String ruolo, Set<CategoriaEntity> categorie) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.hash_password = hash_password;
        this.ruolo = ruolo;
        this.categorie = categorie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getHash_password() {
        return hash_password;
    }

    public void setHash_password(String hash_password) {
        this.hash_password = hash_password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public Set<CategoriaEntity> getCategorie() {
        return categorie;
    }

    public void setCategorie(Set<CategoriaEntity> categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", hash_password='" + hash_password + '\'' +
                ", ruolo='" + ruolo + '\'' +
                ", categorie=" + categorie +
                '}';
    }
}
