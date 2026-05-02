package com.oct.octquiz.Controller.Register;

import com.oct.octquiz.Model.Email.EmailService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void register_shouldReturnToIndexWithOtpWhenValid() throws Exception {
        // Arrange
        when(customUserDetailsService.exists(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/register")
                        .param("registerName", "John")
                        .param("registerSurname", "Doe")
                        .param("registerEmail", "john.doe@example.com")
                        .param("registerPassword", "password123")
                        .param("registerPasswordConfirm", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("askOtp"))
                .andExpect(model().attribute("registerEmail", "john.doe@example.com"));

        verify(emailService).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    void register_shouldFailWhenPasswordsDoNotMatch() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/register")
                        .param("registerName", "John")
                        .param("registerSurname", "Doe")
                        .param("registerEmail", "john.doe@example.com")
                        .param("registerPassword", "password123")
                        .param("registerPasswordConfirm", "differentPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors("registerForm", "registerPasswordConfirm"));
        
        verify(emailService, never()).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    void register_shouldFailWhenEmailAlreadyExists() throws Exception {
        // Arrange
        when(customUserDetailsService.exists("john.doe@example.com")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/register")
                        .param("registerName", "John")
                        .param("registerSurname", "Doe")
                        .param("registerEmail", "john.doe@example.com")
                        .param("registerPassword", "password123")
                        .param("registerPasswordConfirm", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors("registerForm", "registerEmail"));

        verify(emailService, never()).sendEmail(any(SimpleMailMessage.class));
    }
}