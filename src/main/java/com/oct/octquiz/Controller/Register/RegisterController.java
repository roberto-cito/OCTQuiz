package com.oct.octquiz.Controller.Register;

import com.oct.octquiz.Model.Email.EmailService;
import com.oct.octquiz.Model.OTP.OTPManager;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.PasswordUtility;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value; // Import necessario
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashSet;

@Controller
public class RegisterController {

    private final EmailService emailService;
    private final CustomUserDetailsService customUserDetailsService;
    private final String senderEmail;

    public RegisterController(EmailService emailService, CustomUserDetailsService customUserDetailsService, @Value("${spring.mail.username}") String senderEmail) {
        this.emailService = emailService;
        this.customUserDetailsService = customUserDetailsService;
        this.senderEmail = senderEmail;
    }

    @PostMapping("/register")
    public String register(Principal principal, Model model, @Valid @ModelAttribute RegisterForm registerForm, BindingResult bindingResult) {
        if(principal != null) {
            return "redirect:/";
        }
        else {
            if(bindingResult.hasErrors()) {
                model.addAttribute("registerForm", registerForm);
                return "index";
            }
            else {
                if(!registerForm.getRegisterPassword().equals(registerForm.getRegisterPasswordConfirm())) {
                    bindingResult.rejectValue("registerPasswordConfirm", "error.utenteForm", "Le password non coincidono.");
                }
                else if(customUserDetailsService.exists(registerForm.getRegisterEmail())) {
                    bindingResult.rejectValue("registerEmail", "error.utenteForm", "L'email risulta già registrata");
                }
                else {
                    UserEntity userEntity = new UserEntity(registerForm.getRegisterEmail(),registerForm.getRegisterName(),registerForm.getRegisterSurname(),PasswordUtility.hashPassword(registerForm.getRegisterPassword()),"USER", new HashSet<>());
                    OTPManager otpManager = new OTPManager();
                    otpManager.addOTP(registerForm.getRegisterEmail(),userEntity);
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(senderEmail);
                    message.setTo(registerForm.getRegisterEmail());
                    message.setSubject("OCTQUIZ - Conferma il tuo indirizzo email");
                    message.setText("Ciao"+userEntity.getNome()+" "+userEntity.getCognome()+",\n" +
                            "\n" +
                            "Grazie per esserti registrato alla piattaforma OCTQUIZ delle Olimpiadi della Cultura e del Talento.\n" +
                            "\n" +
                            "Per completare la procedura di iscrizione e attivare il tuo account, è necessario verificare questo indirizzo email. Inserisci il codice sottostante nella schermata di registrazione:\n" +
                            "\n" +
                            otpManager.getOTPEmail(registerForm.getRegisterEmail())+"\n" +
                            "Una volta confermato, il tuo account sarà attivo e potrai accedere alla piattaforma.\n" +
                            "\n" +
                            "A presto, Il Team InformaticoOCT");
                    emailService.sendEmail(message);
                    model.addAttribute("askOtp", true);
                    model.addAttribute("registerEmail", registerForm.getRegisterEmail());
                }
                return "index";
            }
        }
    }

    @PostMapping("/retryregister")
    @ResponseBody
    public ResponseEntity<Void> retryRegister(Principal principal, Model model, @ModelAttribute RegisterForm registerForm) {
        if(principal != null || registerForm.getRegisterEmail() == null) {
            return ResponseEntity.badRequest().build(); //Restituisce 403
        }
        else {
            UserEntity userEntity = new UserEntity(registerForm.getRegisterEmail(),registerForm.getRegisterName(),registerForm.getRegisterSurname(),PasswordUtility.hashPassword(registerForm.getRegisterPassword()),"USER", new HashSet<>());
            OTPManager otpManager = new OTPManager();
            otpManager.addOTP(registerForm.getRegisterEmail(),userEntity);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(registerForm.getRegisterEmail());
            message.setSubject("OCTQUIZ - Conferma il tuo indirizzo email");
            message.setText("Ciao"+userEntity.getNome()+" "+userEntity.getCognome()+",\n" +
                    "\n" +
                    "Grazie per esserti registrato alla piattaforma OCTQUIZ delle Olimpiadi della Cultura e del Talento.\n" +
                    "\n" +
                    "Per completare la procedura di iscrizione e attivare il tuo account, è necessario verificare questo indirizzo email. Inserisci il codice sottostante nella schermata di registrazione:\n" +
                    "\n" +
                    otpManager.getOTPEmail(registerForm.getRegisterEmail())+"\n" +
                    "Una volta confermato, il tuo account sarà attivo e potrai accedere alla piattaforma.\n" +
                    "\n" +
                    "A presto, Il Team InformaticoOCT");
            emailService.sendEmail(message);
            return ResponseEntity.ok().build(); //Restituisce 200
        }
    }

    @PostMapping("/confirmregister")
    public String confirmRegister(Principal principal, Model model, @RequestParam String otpCode, @RequestParam String registerEmailOTP, @ModelAttribute RegisterForm registerForm, BindingResult bindingResult) {
        if(principal != null) {
            return "redirect:/";
        }
        else {
            OTPManager otpManager = new OTPManager();
            if(otpManager.getOTPEmail(registerEmailOTP) == null || !otpManager.getOTPEmail(registerEmailOTP).equals(otpCode)) {
                model.addAttribute("askOtp", true);
                model.addAttribute("otpError", true);
                model.addAttribute("registerEmail", registerEmailOTP);
                return "index";
            }
            else {
                UserEntity userEntity=otpManager.getOTPUser(registerEmailOTP);
                customUserDetailsService.save(userEntity);
                otpManager.removeOTPEmail(registerEmailOTP);
                otpManager.removeOTPUser(registerEmailOTP);
                model.addAttribute("otpconfirm", true);
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(senderEmail);
                message.setTo(userEntity.getEmail());
                message.setSubject("Benvenuto ufficialmente su OCTQUIZ");
                message.setText(userEntity.getNome()+" "+userEntity.getCognome()+"!\n" +
                        "\n" +
                        "La tua registrazione è andata a buon fine. Ora fai ufficialmente parte della community digitale delle Olimpiadi della Cultura e del Talento.\n" +
                        "\n" +
                        "Il tuo account è attivo e pronto all'uso. Sei pronto a metterti in gioco e dimostrare il tuo talento?\n" +
                        "\n" +
                        "Clicca qui sotto per effettuare il tuo primo accesso:\n" +
                        "\n" +
                        "https://gare-oct.run.place/octquiz/\n" +
                        "\n" +
                        "In bocca al lupo per le prossime sfide!\n" +
                        "\n" +
                        "Il team InformaticoOCT sezione OCTQUIZ");
                emailService.sendEmail(message);
                return "index";
            }
        }
    }
}