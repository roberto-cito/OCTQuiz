package com.oct.octquiz.Controller.Support;

import com.oct.octquiz.Model.Support.SupportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestOperations;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SupportController {
    private final SupportService supportService;
    private final RestOperations restTemplate;

    @Value("${bot.api.url}")
    private String botUrl;

    public SupportController(SupportService supportService, RestTemplateBuilder restTemplateBuilder) {
        this.supportService = supportService;
        this.restTemplate = restTemplateBuilder.build();
    }

    private void notifyBot(String argomento, String message, String email) {
        Map<String, String> payload = new HashMap<>();
        payload.put("Nome", "OCTQUIZ");
        payload.put("Cognome", "OCTQUIZ");
        payload.put("Email", email);
        payload.put("Argomento", argomento);
        payload.put("Messaggio", message);
        restTemplate.postForEntity(botUrl, payload, String.class);
    }

    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("supportForm", new SupportForm());
        return "support/support";
    }

    @PostMapping("/support")
    public String sendMessage(Model model, @Valid @ModelAttribute SupportForm supportForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("supportForm", supportForm);
            return "support/support";
        }
        if(!supportService.getCategories().contains(supportForm.getCategoria())) {
            bindingResult.rejectValue("categoria","categoria.notfound","La categoria inserita non è valida.");
            model.addAttribute("supportForm", supportForm);
            return "support/support";
        }
        supportService.addNewMessage(supportForm.getEmail(),supportForm.getMessaggio(),supportForm.getCategoria());
        notifyBot(supportForm.getCategoria(),supportForm.getMessaggio(),supportForm.getEmail());
        return "support/confirmsupport";
    }
}
