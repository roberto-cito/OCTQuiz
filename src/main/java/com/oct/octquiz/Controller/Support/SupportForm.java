package com.oct.octquiz.Controller.Support;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SupportForm {
    @NotBlank(message = "L'email del mittente non dev'essere vuota")
    @Size(min=5,max=50,message = "L'email deve essere compresa tra i 5 e i 50 caratteri")
    private String email;

    @NotBlank(message = "La categoria non può essere vuota")
    @Size(min=2,max=30,message = "La categoria deve essere compresa tra i 2 e i 30 caratteri")
    private String categoria;

    @NotBlank(message = "Il messaggio non può essere vuoto")
    @Size(max=300 ,message = "Il messaggio dev'essere massimo 300 caratteri")
    private String messaggio;

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
}
