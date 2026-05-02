package com.oct.octquiz.Controller.Admin;

import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.UserEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ManagerUserController {
    private final CustomUserDetailsService customUserDetailsService;
    private final SessionRegistry sessionRegistry;

    public ManagerUserController(CustomUserDetailsService customUserDetailsService, SessionRegistry sessionRegistry) {
        this.customUserDetailsService = customUserDetailsService;
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/admin/users")
    public String manager(Principal principal, Model model) {
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("users", customUserDetailsService.findAll());
        return "admin/users";
    }

    @PostMapping("/admin/remove-user")
    public String removeUser(Principal principal, Model model, @RequestParam String email) {
        customUserDetailsService.removeUser(customUserDetailsService.findByEmail(email));
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for (Object p : allPrincipals) {
            if (p instanceof UserDetails user) {
                if (user.getUsername().equals(email)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(p, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
        model.addAttribute("users", customUserDetailsService.findAll());
        model.addAttribute("message", "Utente rimosso con successo");
        model.addAttribute("class", "alert-success");
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        return "admin/users";
    }

    @PostMapping("/admin/update-user")
    public String updateUser(Principal principal, Model model, @RequestParam String email, @RequestParam String newMail, @RequestParam String nome, @RequestParam String cognome, @RequestParam boolean ruolo) {
        UserEntity userEntity = customUserDetailsService.findByEmail(email);
        userEntity.setNome(nome);
        userEntity.setCognome(cognome);
        userEntity.setEmail(newMail);
        userEntity.setRuolo(ruolo ? "ADMIN" : "USER");
        customUserDetailsService.save(userEntity);
        model.addAttribute("users", customUserDetailsService.findAll());
        model.addAttribute("message", "Utente aggiornato con successo");
        model.addAttribute("class", "alert-success");
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        return "admin/users";
    }

    @PostMapping("/admin/reset-user-results")
    public String removeUserResult(Principal principal, Model model, @RequestParam String email) {
        customUserDetailsService.resetCategories(email);
        model.addAttribute("users", customUserDetailsService.findAll());
        model.addAttribute("message", "Utente aggiornato con successo");
        model.addAttribute("class", "alert-success");
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        return "admin/users";
    }
}
