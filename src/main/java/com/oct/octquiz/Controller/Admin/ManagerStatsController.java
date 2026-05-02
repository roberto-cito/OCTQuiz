package com.oct.octquiz.Controller.Admin;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import com.oct.octquiz.Model.Categoria.CategoryService;
import com.oct.octquiz.Model.Categoria.Stat.StatCategoriaService;
import com.oct.octquiz.Model.Domanda.DomandaService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ManagerStatsController {
    private final CustomUserDetailsService customUserDetailsService;
    private final StatCategoriaService statCategoriaService;
    private final CategoryService categoryService;
    private final DomandaService domandaService;

    public ManagerStatsController(CustomUserDetailsService customUserDetailsService, StatCategoriaService statCategoriaService, CategoryService categoryService, DomandaService domandaService) {
        this.customUserDetailsService = customUserDetailsService;
        this.statCategoriaService = statCategoriaService;
        this.categoryService = categoryService;
        this.domandaService = domandaService;
    }

    @GetMapping("/admin/stats")
    public String getter(Principal principal, Model model) {
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/stats";
    }

    @PostMapping("/admin/stats")
    public String seeCategoryStats(Principal principal, Model model, @RequestParam Integer id) {
        CategoriaEntity categoriaEntity=categoryService.findById(id);
        if(categoriaEntity==null) {
            return "redirect:/admin/stats";
        }
        model.addAttribute("user",customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("categoria", categoriaEntity);
        model.addAttribute("stats",statCategoriaService.getStatByCategoria(categoriaEntity));
        model.addAttribute("domande",domandaService.findAllByCategoria(categoriaEntity));
        return "admin/stats-category";
    }

    @PostMapping("/admin/remove-stat")
    public String removeCategoryStat(Principal principal, Model model, @RequestParam Integer id) {
        CategoriaEntity categoriaEntity=categoryService.findById(id);
        if(categoriaEntity==null) {
            return "redirect:/admin/stats";
        }
        statCategoriaService.deleteAllByCategoria(categoriaEntity);

        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("categoria", categoriaEntity);
        model.addAttribute("stats", statCategoriaService.getStatByCategoria(categoriaEntity));
        model.addAttribute("domande", domandaService.findAllByCategoria(categoriaEntity));
        model.addAttribute("message", "Statistiche rimosse con successo");
        return "admin/stats-category";
    }
}
