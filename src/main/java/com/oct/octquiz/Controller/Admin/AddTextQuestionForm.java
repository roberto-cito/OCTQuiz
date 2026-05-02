package com.oct.octquiz.Controller.Admin;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class AddTextQuestionForm {
    private int id_categoria;

    @NotBlank(message = "Il testo della domanda non può essere vuoto")
    private String domanda;

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
}
