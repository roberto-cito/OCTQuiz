package com.oct.octquiz.Controller.HomePage;

import com.oct.octquiz.Controller.Register.RegisterForm;
import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import com.oct.octquiz.Model.Categoria.CategoriaRepository;
import com.oct.octquiz.Model.Categoria.CategoryService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final CustomUserDetailsService customUserDetailsService;
    private final CategoryService categoryService;

    public HomeController(CustomUserDetailsService customUserDetailsService, CategoryService categoryService) {
        this.customUserDetailsService = customUserDetailsService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String homepage(Model model, Principal principal, HttpServletRequest request) {
        // Forza la generazione del token CSRF (e della sessione) PRIMA che Thymeleaf inizi a flushare l'HTML.
        // Questo previene l'errore "Cannot create a session after the response has been committed" 
        // che causa l'interruzione della connessione (ERR_HTTP2_PROTOCOL_ERROR).
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            csrf.getToken();
        }

        if(principal != null) {
            return "redirect:/home";
        }
        else {
            model.addAttribute("registerForm", new RegisterForm());
            return "index";
        }
    }

    @GetMapping("/home")
    public String getLoggedHomePage(Principal principal, Model model) {
        // Recupera l'utente loggato
        UserEntity userEntity = customUserDetailsService.findByEmail(principal.getName());
        model.addAttribute("user", userEntity);
        if(userEntity.getRuolo().equals("USER")) {
            // Recupera tutte le categorie (quiz) disponibili
            List<CategoriaEntity> allCategories = categoryService.findAll();
            allCategories.removeIf(categoria -> !categoria.isVisibile());
            model.addAttribute("categories", allCategories);

            // Crea un Set di ID delle categorie completate per un controllo veloce nella view
            Set<Integer> completedCategoryIds = userEntity.getCategorie().stream()
                    .map(CategoriaEntity::getId)
                    .collect(Collectors.toSet());
            model.addAttribute("completedCategoryIds", completedCategoryIds);
            return "home";
        }
        else if(userEntity.getRuolo().equals("ADMIN")) {
            return "redirect:/admin";
        }
        else {
            return "redirect:/";
        }
    }
}