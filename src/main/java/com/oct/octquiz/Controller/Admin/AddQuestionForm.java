package com.oct.octquiz.Controller.Admin;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class AddQuestionForm {
    private int id_categoria;

    @NotBlank(message = "Il testo della domanda non può essere vuoto")
    private String domanda;

    private MultipartFile foto;

    private MultipartFile audio;

    @NotBlank(message = "La risposta non può essere vuota")
    private String risposta1;

    @NotBlank(message = "La risposta non può essere vuota")
    private String risposta2;

    @NotBlank(message = "La risposta non può essere vuota")
    private String risposta3;

    @NotBlank(message = "La risposta non può essere vuota")
    private String risposta4;

    private int rispostaCorretta;

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getDomanda() {
        return domanda;
    }

    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }

    public MultipartFile getFoto() {
        return foto;
    }

    public void setFoto(MultipartFile foto) {
        this.foto = foto;
    }

    public MultipartFile getAudio() {
        return audio;
    }

    public void setAudio(MultipartFile audio) {
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
}
