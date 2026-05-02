package com.oct.octquiz.Controller.EditUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditPasswordForm {
   @NotBlank(message="C'è stato qualche problema nella richiesta, ricarica e riprova")
    public String email;

    @NotBlank(message="Devi inserire la password attuale")
    public String oldPassword;

    @NotBlank(message="Devi inserire una nuova password")
    @Size(min=8,max=30,message="La password deve essere compresa tra i 8 e i 30 caratteri")
    public String newPassword;

    @NotBlank(message="Devi confermare la nuova password")
    @Size(min=8,max=30,message="La password deve essere compresa tra i 8 e i 30 caratteri")
    public String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
