package com.oct.octquiz.Controller.Register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {
    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(min=2,max=30,message = "Il nome dev'essere compreso tra i 2 e i 30 caratteri")
    private String registerName;

    @NotBlank(message = "Il cognome non può essere vuoto")
    @Size(min=2,max=30,message = "Il cognome dev'essere compreso tra i 2 e i 30 caratteri")
    private String registerSurname;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Inserisci un indirizzo mail valido")
    private String registerEmail;

    @NotBlank(message = "Il campo password non può essere vuoto")
    @Size(min=8,message = "La password non può essere minore di 8 caratteri")
    private String registerPassword;

    @NotBlank(message = "Il campo conferma password non può essere vuoto")
    private String registerPasswordConfirm;

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getRegisterSurname() {
        return registerSurname;
    }

    public void setRegisterSurname(String registerSurname) {
        this.registerSurname = registerSurname;
    }

    public String getRegisterEmail() {
        return registerEmail;
    }

    public void setRegisterEmail(String registerEmail) {
        this.registerEmail = registerEmail;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }

    public String getRegisterPasswordConfirm() {
        return registerPasswordConfirm;
    }

    public void setRegisterPasswordConfirm(String registerPasswordConfirm) {
        this.registerPasswordConfirm = registerPasswordConfirm;
    }
}
