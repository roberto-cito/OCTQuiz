package com.oct.octquiz.Controller.Admin;
import com.oct.octquiz.Model.Support.SupportService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import com.oct.octquiz.UserStats;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    private final CustomUserDetailsService customUserDetailsService;
    private final SupportService supportService;
    private final UserStats userStats;

    public AdminController(CustomUserDetailsService customUserDetailsService, SupportService supportService, UserStats userStats) {
        this.customUserDetailsService = customUserDetailsService;
        this.supportService = supportService;
        this.userStats = userStats;
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        UserEntity userEntity = customUserDetailsService.findByEmail(principal.getName());
        model.addAttribute("user", userEntity);
        model.addAttribute("numberofusers", customUserDetailsService.getNumberOfUsers());
        model.addAttribute("numberofmessages", supportService.getNumberOfMessages());
        List<String> activeUsers=userStats.getUsersOnline(principal.getName());
        model.addAttribute("usersonline", activeUsers);
        model.addAttribute("numberofactiveusers", activeUsers.size());
        return "admin/home";
    }
}
