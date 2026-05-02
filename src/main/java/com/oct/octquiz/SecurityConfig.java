package com.oct.octquiz;

import org.springframework.core.env.Environment;
import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final Environment environment;

    public SecurityConfig(UserDetailsService userDetailsService, Environment environment) {
        this.userDetailsService = userDetailsService;
        this.environment = environment;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            http.csrf(csrf -> csrf.disable());
        }

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/quiz/**").hasRole("USER")
                        .requestMatchers("/home","/account").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/ping","/favicon.ico","/", "/register", "/retryregister", "/confirmregister", "/login", "/forgot-password", "/confirm-password", "/css/**", "/photo/**","/uploads/**","/maintenance","/IfYouKnowThisURLShootYourself","/support", "/faq","/terms","/privacy","/contatti","/cookie-policy","/info-cookie","/sponsor/**").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN") // permetti accesso agli endpoint Prometheus solo agli admin
                        .anyRequest().authenticated() // tutte le altre pagine richiedono login
                )
                .formLogin(form -> form
                        .loginPage("/")     // tua pagina custom
                        .loginProcessingUrl("/login") // gestito da Spring Security, NON devi scriverlo tu
                        .failureUrl("/?error=true")
                        .defaultSuccessUrl("/home", true) // redirect dopo login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .permitAll()
                ).sessionManagement(session -> session
                        .maximumSessions(-1) // Permette sessioni illimitate
                        .sessionRegistry(sessionRegistry()) // Collega il registro!
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ForwardedHeaderFilter());
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
