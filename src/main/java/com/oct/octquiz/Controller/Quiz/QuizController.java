package com.oct.octquiz.Controller.Quiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import com.oct.octquiz.Model.Categoria.CategoriaRepository;
import com.oct.octquiz.Model.Categoria.CategoryService;
import com.oct.octquiz.Model.Categoria.Stat.StatCategoriaEntity;
import com.oct.octquiz.Model.Categoria.Stat.StatCategoriaService;
import com.oct.octquiz.Model.Domanda.DomandaEntity;
import com.oct.octquiz.Model.Domanda.DomandaRepository;
import com.oct.octquiz.Model.Domanda.DomandaService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class QuizController {
    private final CategoryService categoryService;
    private final DomandaService domandaService;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final StatCategoriaService statCategoriaService;

    public QuizController(CategoryService categoryService, DomandaService domandaService, ObjectMapper objectMapper,
            CustomUserDetailsService customUserDetailsService, StatCategoriaService statCategoriaService) {
        this.categoryService = categoryService;
        this.domandaService = domandaService;
        this.objectMapper = objectMapper;
        this.customUserDetailsService = customUserDetailsService;
        this.statCategoriaService = statCategoriaService;
    }

    @GetMapping("/quiz")
    public String returnInstruction(Principal principal, Model model, @RequestParam int id) {
        CategoriaEntity categoria = categoryService.findById(id);
        if (categoria == null) {
            return "redirect:/";
        } else {
            UserEntity user;
            try {
                user = customUserDetailsService.findByEmail(principal.getName());
            } catch (UsernameNotFoundException e) {
                return "redirect:/";
            }
            model.addAttribute("categoria", categoria);
            model.addAttribute("user", user);
            return "quiz/prequiz";
        }
    }

    @PostMapping("/quiz")
    public String startQuiz(Principal principal, Model model, @RequestParam int id) {
        CategoriaEntity categoriaEntity = categoryService.findById(id);
        if (categoriaEntity == null) {
            return "redirect:/";
        } else {
            List<DomandaEntity> domande = domandaService.findAllByCategoria(categoriaEntity);
            if (domande.isEmpty()) {
                return "redirect:/";
            } else {
                UserEntity user;
                try {
                    user = customUserDetailsService.findByEmail(principal.getName());
                } catch (UsernameNotFoundException e) {
                    return "redirect:/";
                }
                model.addAttribute("user", user);
                model.addAttribute("categoria", categoriaEntity);
                model.addAttribute("domande", domande);
                return "quiz/quiz";
            }
        }
    }

    @PostMapping("/quiz/submit")
    public String submitQuiz(Principal principal, Model model, @RequestParam int categoriaId,
            @RequestParam String answers) {
        CategoriaEntity categoriaEntity = categoryService.findById(categoriaId);
        if (categoriaEntity == null) {
            return "redirect:/";
        }

        try {
            Map<String, Integer> userAnswers = objectMapper.readValue(answers,
                    new TypeReference<Map<String, Integer>>() {
                    });
            List<DomandaEntity> domande = domandaService.findAllByCategoria(categoriaEntity);
            Map<String, Boolean> risposte = new HashMap<>();

            int punteggio = 0;
            int maxPunteggio = domande.stream().filter(domandaEntity -> !domandaEntity.isOnlyText()).toList().size()
                    * 8;

            for (DomandaEntity d : domande) {
                if (d.isOnlyText())
                    continue;
                Integer rispostaData = userAnswers.get(String.valueOf(d.getId()));
                if (rispostaData != null) {
                    if (rispostaData == d.getRispostaCorretta()) {
                        risposte.put(String.valueOf(d.getId()), true);
                        punteggio += 8;
                    } else {
                        risposte.put(String.valueOf(d.getId()), false);
                        punteggio -= 2;
                    }
                } else
                    risposte.put(String.valueOf(d.getId()), null);
            }

            Gson gson = new Gson();
            statCategoriaService.save(new StatCategoriaEntity(categoriaEntity, gson.toJson(risposte)));
            customUserDetailsService.addCategory(categoriaEntity, principal.getName());

            model.addAttribute("userAnswers", userAnswers);
            model.addAttribute("domande", domande);
            model.addAttribute("punteggio", punteggio);
            model.addAttribute("totale", maxPunteggio);
            model.addAttribute("categoria", categoriaEntity);
            model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));

            return "quiz/result"; // Dobbiamo creare questa pagina o redirigere altrove
        } catch (JsonProcessingException e) {
            return "redirect:/";
        }
    }
}
