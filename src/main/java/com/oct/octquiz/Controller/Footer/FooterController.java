package com.oct.octquiz.Controller.Footer;

import com.oct.octquiz.Controller.Register.RegisterForm;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashSet;

@Controller
public class FooterController {

    private final CustomUserDetailsService customUserDetailsService;

    public FooterController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/faq")
    public String getFAQPage(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("isLogged",true);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("isLogged",false);
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "faq";
    }

    @GetMapping("/privacy")
    public String getPrivacyPage(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("isLogged",true);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("isLogged",false);
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "privacy";
    }

    @GetMapping("/cookie-policy")
    public String getCookiePolicyPage(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("isLogged",true);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("isLogged",false);
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "cookie-policy";
    }

    @GetMapping("/terms")
    public String getTermsPage(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("isLogged",true);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("isLogged",false);
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "terms";
    }

    @GetMapping("/contatti")
    public String getContactsPage(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("isLogged",true);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("isLogged",false);
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "contatti";
    }

    @GetMapping("/info-cookie")
    public String getCookiePage(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = customUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("isLogged",true);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("isLogged",false);
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "cookie-policy";
    }
}
