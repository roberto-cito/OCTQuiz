package com.oct.octquiz.Controller.EditUser;

import com.google.gson.Gson;
import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import com.oct.octquiz.Model.Email.EmailService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.PasswordUtility;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.security.Principal;
import java.util.Optional;

@Controller
public class EditUserController {
    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;
    private final String senderEmail;

    public EditUserController(CustomUserDetailsService customUserDetailsService, EmailService emailService,  @Value("${spring.mail.username}") String senderEmail) {
        this.customUserDetailsService = customUserDetailsService;
        this.emailService = emailService;
        this.senderEmail = senderEmail;
    }

    //Carica la pagina
    @GetMapping("/account")
    public String getEditUser(Model model, Principal principal, @ModelAttribute EditPasswordForm editPasswordForm) {
        try {
            UserEntity user = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("editPasswordForm", editPasswordForm);
            editPasswordForm.setEmail(user.getEmail());
            return "edituser";
        } catch (UsernameNotFoundException ue) {
            return "redirect:/";
        }
    }

    //Modifica Password
    @PostMapping("/account/update-password")
    public String updatePassword(Model model, Principal principal, @Valid @ModelAttribute EditPasswordForm editPasswordForm, BindingResult bindingResult) {
        editPasswordForm.setEmail(editPasswordForm.email);
        if(bindingResult.hasErrors()) { //Controlla dimensioni e se i campi sono vuoti
             model.addAttribute("editPasswordForm", editPasswordForm);
        }
        UserEntity user = customUserDetailsService.findByEmail(principal.getName());
        if(!editPasswordForm.newPassword.equals(editPasswordForm.confirmPassword)) { //Controlla se le password coincidono
            bindingResult.rejectValue("confirmPassword","error.confirmPassword","Le password non coincidono");
            model.addAttribute("editPasswordForm", editPasswordForm);
        }
        else if(!PasswordUtility.checkPassword(editPasswordForm.oldPassword, user.getHash_password())) { //Controllo se la vecchia password corrisponde
            bindingResult.rejectValue("oldPassword","error.oldPassword","La password attuale è errata");
            model.addAttribute("editPasswordForm", editPasswordForm);
        }
        else if(PasswordUtility.checkPassword(editPasswordForm.newPassword,user.getHash_password())) {
            bindingResult.rejectValue("confirmPassword","error.confirmPassword","La nuova password deve essere diversa dalla vecchia");
            model.addAttribute("editPasswordForm", editPasswordForm);
        }
        else { //Nel caso sia tutto coretto aggiorno la password
            customUserDetailsService.updatePassword(editPasswordForm.newPassword, editPasswordForm.email);
            model.addAttribute("editPasswordSuccess",true);
        }
        model.addAttribute("user", user);
        return "edituser";
    }

    //Scarica i dati in formato json
    @GetMapping("/account/export")
    public ResponseEntity<byte[]> downloadData(Model model, Principal principal, @ModelAttribute EditPasswordForm editPasswordForm) {
        try {
            UserEntity userEntity = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("user", userEntity);
            model.addAttribute("editPasswordForm", editPasswordForm);
            userEntity.setHash_password("La password viene salvata come hash, neanche noi la sappiamo");
            String nome_file="user_exported_data.json";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + nome_file);
            Gson gson = new Gson();
            for(CategoriaEntity categoria : userEntity.getCategorie()) categoria.setVisibile(true);
            return ResponseEntity.ok().headers(headers).body(gson.toJson(userEntity).getBytes());
        }catch (UsernameNotFoundException ue) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));

            // Ritorna uno status 302 (Found) o 303 (See Other)
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }

    //Elimina l'account
    @PostMapping("/account/delete")
    public String removeAccount(Model model, Principal principal, @RequestParam String password, @ModelAttribute EditPasswordForm editPasswordForm, HttpSession session) {
        try {
            UserEntity userEntity = customUserDetailsService.findByEmail(principal.getName());
            if(PasswordUtility.checkPassword(password, userEntity.getHash_password())) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(this.senderEmail);
                message.setTo(userEntity.getEmail());
                message.setSubject("Cancellazione account");
                message.setText("Il tuo account è stato eliminato con successo.");
                emailService.sendEmail(message);
                session.invalidate();
                customUserDetailsService.removeUser(userEntity);
                return "redirect:/";
            }
            else {
                model.addAttribute("user", userEntity);
                model.addAttribute("editPasswordForm", editPasswordForm);
                model.addAttribute("deleteError", true);
                return "edituser";
            }
        } catch (UsernameNotFoundException ue) {
            return "redirect:/";
        }
    }

    @PostMapping("/account/update")
    public String editAccount(Model model, Principal principal, @ModelAttribute EditPasswordForm editPasswordForm, @RequestParam String nome, @RequestParam String cognome) {
        try {
            UserEntity userEntity = customUserDetailsService.findByEmail(principal.getName());
            if (nome.isEmpty()) {
                model.addAttribute("updateError","Il campo nome è vuoto.");
            }
            else if (cognome.isEmpty()) {
                model.addAttribute("updateError","Il campo cognome è vuoto.");
            }
            else if (nome.length()>30 || nome.length()<2) {
                model.addAttribute("updateError","Il nome dev'essere compreso tra i 2 e i 30 caratteri.");
            }
            else if (cognome.length()>30 || cognome.length()<2) {
                model.addAttribute("updateError","Il cognome deve essere compreso tra i 2 e i 30 caratteri.");
            }
            else {
                userEntity.setNome(nome);
                userEntity.setCognome(cognome);
                customUserDetailsService.save(userEntity);
                model.addAttribute("updateSuccess",true);
            }
            model.addAttribute("user",userEntity);
            model.addAttribute("editPasswordForm", editPasswordForm);
            return "edituser";
        } catch (UsernameNotFoundException ue) {
            return "redirect:/";
        }
    }
}
