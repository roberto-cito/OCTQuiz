package com.oct.octquiz.DBPopulator;

import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.PasswordUtility;
import com.oct.octquiz.Model.User.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AdminPopulator {
    private final CustomUserDetailsService customUserDetailsService;

    public AdminPopulator(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService=customUserDetailsService;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            try {
                customUserDetailsService.loadUserByUsername("robbencito@gmail.com");
                System.out.println("Admin già esistente");
            } catch (UsernameNotFoundException e) {
                UserEntity user=new UserEntity("robbencito@gmail.com","Roberto","Cito", PasswordUtility.hashPassword("testest"),"ADMIN",null);
                customUserDetailsService.addUser(user);
                System.out.println("Admin aggiunto");
            }
        };
    }
}
