package com.oct.octquiz.Controller.Admin;

import com.oct.octquiz.Model.Support.SupportEntity;
import com.oct.octquiz.Model.Support.SupportService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ManagerSupportController {
    private final SupportService supportService;
    private final CustomUserDetailsService customUserDetailsService;

    public ManagerSupportController(SupportService supportService, CustomUserDetailsService customUserDetailsService) {
        this.supportService = supportService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/admin/support")
    public String getManagerSupport(Model model, Principal principal) {
        UserEntity userEntity = customUserDetailsService.findByEmail(principal.getName());
        model.addAttribute("user", userEntity);
        List<SupportEntity> messaggi=supportService.getAllMessages();
        model.addAttribute("messaggi",messaggi);
        return "admin/supportmessages";
    }
    
    @PostMapping("/admin/remove-message")
    public String removeMessage(Model model, @RequestParam Integer id) {
        supportService.removeMessage(id);
        return "redirect:/admin/support";
    }
    
    @PostMapping("/admin/remove-all-messages")
    public String removeAllMessages(Model model) {
        supportService.removeAllMessages();
        return "redirect:/admin/support";
    }
}
