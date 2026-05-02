package com.oct.octquiz.Controller.Admin;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import com.oct.octquiz.Model.Categoria.CategoryService;
import com.oct.octquiz.Model.Categoria.Stat.StatCategoriaService;
import com.oct.octquiz.Model.Domanda.DomandaEntity;
import com.oct.octquiz.Model.Domanda.DomandaService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class ManagerCategoriesController {
    private final CategoryService categoryService;
    private final DomandaService domandaService;
    private final CustomUserDetailsService customUserDetailsService;
    private final StatCategoriaService statCategoriaService;

    public ManagerCategoriesController(CategoryService categoryService, DomandaService domandaService, CustomUserDetailsService customUserDetailsService, StatCategoriaService statCategoriaService) {
        this.categoryService = categoryService;
        this.domandaService = domandaService;
        this.customUserDetailsService = customUserDetailsService;
        this.statCategoriaService = statCategoriaService;
    }

    @GetMapping("/admin/categories")
    public String categories(Model model, Principal principal) {
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories";
    }

    @GetMapping("/admin/category")
    public String category(Model model, Principal principal, @RequestParam Integer id) {
        CategoriaEntity categoria=categoryService.findById(id);
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("category", categoria);
        model.addAttribute("domande", domandaService.findAllByCategoria(categoria));
        return "admin/category";
    }

    @PostMapping("/admin/add-category")
    public String addCategory(Principal principal, Model model, @RequestParam String nome, @RequestParam Integer tempo, @RequestParam(defaultValue = "false") boolean visibile) {
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("categories", categoryService.findAll());
        if(nome.isEmpty()) {
            model.addAttribute("message","Il nome della categoria non può essere vuota");
            model.addAttribute("class","alert-danger");
        }
        else if(tempo == null) {
            model.addAttribute("message","Il tempo non può essere vuoto");
            model.addAttribute("class","alert-danger");
        }
        else {
            CategoriaEntity categoria=new CategoriaEntity(nome, tempo, visibile);
            categoryService.save(categoria);
            return "redirect:/admin/categories";
        }
        return "admin/categories";
    }

    @PostMapping("/admin/remove-category")
    public String removeCategory(Principal principal, Model model, @RequestParam Integer id) throws IOException {
        if(id == null) {
            return "redirect:/admin/categories";
        }
        else {
            CategoriaEntity categoriaEntity=categoryService.findById(id);
            statCategoriaService.deleteAllByCategoria(categoriaEntity);
            domandaService.deleteAll(categoriaEntity);
            customUserDetailsService.removeAllCompleteCategory(categoriaEntity);
            categoryService.deleteById(id);
            model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
            model.addAttribute("categories", categoryService.findAll());
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/admin/update-category")
    public String updateCategory(Principal principal, Model model, @RequestParam Integer id, @RequestParam String nome, @RequestParam Integer tempo, @RequestParam(defaultValue = "false") boolean visibile) {
        CategoriaEntity categoriaEntity = categoryService.findById(id);
        categoriaEntity.setNome(nome);
        categoriaEntity.setTempo(tempo);
        categoriaEntity.setVisibile(visibile);
        categoryService.update(categoriaEntity);
        model.addAttribute("user", customUserDetailsService.findByEmail(principal.getName()));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("message", "Categoria aggiornata con successo");
        model.addAttribute("class", "alert-success");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/add-question")
    public ResponseEntity<byte[]> addQuestion(Principal principal, Model model, @Valid @ModelAttribute AddQuestionForm addQuestionForm, BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()) {
            model.addAttribute("addQuestionForm", addQuestionForm);
            return ResponseEntity.badRequest().build();
        }
        else {
            String path=null;
            if(!addQuestionForm.getFoto().isEmpty()) {
                path="uploads/"+addQuestionForm.getId_categoria()+addQuestionForm.getRispostaCorretta()+addQuestionForm.getFoto().getOriginalFilename();
                path=path.replace(" ","");
                if(Files.exists(Paths.get(path))) {
                    return ResponseEntity.badRequest().build();
                }
                Files.write(Path.of(path),addQuestionForm.getFoto().getBytes());
            }
            String pathAudio = null;
            if(!addQuestionForm.getAudio().isEmpty()) {
                pathAudio = "uploads/"+addQuestionForm.getId_categoria()+addQuestionForm.getRispostaCorretta()+addQuestionForm.getAudio().getOriginalFilename();
                pathAudio = pathAudio.replace(" ","");
                if(Files.exists(Paths.get(pathAudio))) {
                    return ResponseEntity.badRequest().build();
                }
                Files.write(Path.of(pathAudio),addQuestionForm.getAudio().getBytes());
            }
            CategoriaEntity categoria=categoryService.findById(addQuestionForm.getId_categoria());
            DomandaEntity newDomanda=new DomandaEntity(categoria,addQuestionForm.getDomanda(),path,pathAudio,addQuestionForm.getRisposta1(),addQuestionForm.getRisposta2(),addQuestionForm.getRisposta3(),addQuestionForm.getRisposta4(),addQuestionForm.getRispostaCorretta());
            domandaService.save(newDomanda);
            statCategoriaService.deleteAllByCategoria(categoria);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/admin/edit-question")
    public ResponseEntity<byte[]> editQuestion(Principal principal, Model model, @RequestParam Integer id_categoria, @RequestParam Integer id_domanda, @RequestParam String domanda, @RequestParam String risposta1, @RequestParam String risposta2, @RequestParam String risposta3, @RequestParam String risposta4, @RequestParam Integer rispostaCorretta, @RequestParam(required = false) MultipartFile foto, @RequestParam(required = false) MultipartFile audio, @RequestParam(defaultValue = "false") boolean delete_photo, @RequestParam(defaultValue = "false") boolean delete_audio) throws IOException {
        if(id_categoria == null || id_domanda == null || domanda.isEmpty() || risposta1.isEmpty() || risposta2.isEmpty() || risposta3.isEmpty() || risposta4.isEmpty() || rispostaCorretta == null) return ResponseEntity.badRequest().build();
        CategoriaEntity categoria=categoryService.findById(id_categoria);
        if(categoria==null) return ResponseEntity.badRequest().build();
        DomandaEntity domandaEntity=domandaService.findByCategoriaAndId(categoria,id_domanda);
        if(domandaEntity==null) return ResponseEntity.badRequest().build();
        if(delete_photo) domandaEntity.setFoto(null);
        if(delete_audio) domandaEntity.setAudio(null);
        if(foto != null && !foto.isEmpty()) {
            String path="uploads/"+id_categoria+rispostaCorretta+foto.getOriginalFilename();
            path=path.replace(" ","");
            Files.write(Path.of(path),foto.getBytes());
            domandaEntity.setFoto(path);
        }
        
        if(audio != null && !audio.isEmpty()) {
            String pathAudio="uploads/"+id_categoria+rispostaCorretta+audio.getOriginalFilename();
            pathAudio=pathAudio.replace(" ","");
            Files.write(Path.of(pathAudio),audio.getBytes());
            domandaEntity.setAudio(pathAudio);
        }
        
        domandaEntity.setDomanda(domanda);
        domandaEntity.setRisposta1(risposta1);
        domandaEntity.setRisposta2(risposta2);
        domandaEntity.setRisposta3(risposta3);
        domandaEntity.setRisposta4(risposta4);
        domandaEntity.setRispostaCorretta(rispostaCorretta);
        domandaEntity.setOnlyText(false);
        domandaService.update(domandaEntity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/add-text-question")
    public ResponseEntity<byte[]> addTextQuestion(Principal principal, Model model, @Valid @ModelAttribute AddTextQuestionForm addTextQuestionForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("addTextQuestionForm", addTextQuestionForm);
            return ResponseEntity.badRequest().build();
        }
        else {
            CategoriaEntity categoria=categoryService.findById(addTextQuestionForm.getId_categoria());
            DomandaEntity newDomanda=new DomandaEntity(categoria,addTextQuestionForm.getDomanda());
            domandaService.save(newDomanda);
            statCategoriaService.deleteAllByCategoria(categoria);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/admin/edit-text-question")
    public ResponseEntity<byte[]> editTextQuestion(Principal principal, Model model, @RequestParam Integer id_categoria, @RequestParam Integer id_domanda, @RequestParam String testo) {
        if(id_categoria == null || id_domanda == null || testo.isEmpty()) return ResponseEntity.badRequest().build();
        CategoriaEntity categoria=categoryService.findById(id_categoria);
        if(categoria==null) return ResponseEntity.badRequest().build();
        DomandaEntity domanda=domandaService.findByCategoriaAndId(categoria,id_domanda);
        if(domanda==null) return ResponseEntity.badRequest().build();
        domanda.setDomanda(testo);
        domanda.setOnlyText(true);
        domandaService.update(domanda);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/remove-question")
    public ResponseEntity<byte[]> removeQuestion(Model model, @RequestParam Integer id_domanda, @RequestParam Integer id_categoria) throws IOException {
        if(id_domanda == null || id_categoria == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            CategoriaEntity categoria=categoryService.findById(id_categoria);
            DomandaEntity domandaEntity=domandaService.findByCategoriaAndId(categoria,id_domanda);
            domandaService.delete(domandaEntity);
            statCategoriaService.deleteAllByCategoria(categoria);
            return ResponseEntity.ok().build();
        }
    }
}
