package com.oct.octquiz.Controller.MissingPassword;

import com.oct.octquiz.Controller.Register.RegisterForm;
import com.oct.octquiz.Model.Email.EmailService;
import com.oct.octquiz.Model.OTP.OTPManager;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.PasswordUtility;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MissingPasswordController {

    private final String senderemail;
    private final EmailService emailService;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public MissingPasswordController(@Value("${spring.mail.username}") String senderemail, EmailService emailService, CustomUserDetailsService customUserDetailsService) {
        this.senderemail = senderemail;
        this.emailService = emailService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/forgot-password")
    public String getRecoveryPassword(Model model, Principal principal, @RequestParam String email, @ModelAttribute ConfirmPasswordForm confirmPasswordForm, BindingResult bindingResult, @ModelAttribute RegisterForm registerForm) {
        if(principal != null) {
            return "redirect:/";
        }
        else {
            try {
                UserEntity userEntity = customUserDetailsService.findByEmail(email);
                OTPManager otpManager = new OTPManager();
                otpManager.addOTP(email, userEntity);
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(this.senderemail);
                message.setTo(email);
                message.setSubject("OCTQUIZ - Istruzioni per reimpostare la password");
                message.setText("Ciao"+userEntity.getNome()+" "+userEntity.getCognome()+",\n" +
                        "\n" +
                        "Abbiamo ricevuto una richiesta per reimpostare la password del tuo account OCTQUIZ.\n" +
                        "\n" +
                        "Per procedere e scegliere una nuova password, utilizza il seguente codice di sicurezza:\n" +
                        "\n" +
                        otpManager.getOTPEmail(email)+"\n" +
                        "Questo codice è necessario per garantire la sicurezza del tuo account.\n" +
                        "\n" +
                        "Nota: Se non sei stato tu a richiedere questa modifica, ignora pure questa email e/o manda una segnalazione a noi. La tua password attuale rimarrà invariata e il tuo account è al sicuro.\n" +
                        "\n" +
                        "Cordiali saluti, Il Team InformaticoOCT");
                emailService.sendEmail(message);
                model.addAttribute("email", email);
                return "ForgotPassword/forgot-password";
            }
            catch (UsernameNotFoundException ue) { //Altrimenti da errore
                model.addAttribute("mailnotfound", true);
                model.addAttribute("registerForm", registerForm);
                return "index";
            }
        }
    }

    @PostMapping("confirm-password")
    public String confirmNewPassword(Model model, Principal principal, @Valid @ModelAttribute ConfirmPasswordForm confirmPasswordForm, BindingResult bindingResult) {
        if(principal != null) {
            return "redirect:/";
        }
        else {
            if(bindingResult.hasErrors()) { //Se il form non è corretto da errore
                model.addAttribute("confirmPasswordForm", confirmPasswordForm);
                return "ForgotPassword/forgot-password";
            }
            else { //Se le password non coincidono da errore
                if(!confirmPasswordForm.getNewPassword().equals(confirmPasswordForm.getConfirmNewPassword())) {
                    bindingResult.rejectValue("confirmNewPassword", "error.confirmNewPassword","Le password non coincidono");
                    return "ForgotPassword/forgot-password";
                }
                OTPManager otpManager = new OTPManager();
                String code=otpManager.getOTPEmail(confirmPasswordForm.getEmail());
                //Se l'OTP non è corretto da errore
                if(!code.equals(confirmPasswordForm.getOtp())) {
                    bindingResult.rejectValue("otp","error.otp","L'OTP inserito è errato, controlla e riprova.");
                    return "ForgotPassword/forgot-password";
                }
                UserEntity user;
                try {
                    user = customUserDetailsService.findByEmail(confirmPasswordForm.getEmail());
                } catch (UsernameNotFoundException ue) {  //Non dovrebbe mai succedere, ma se succede, sparati
                    bindingResult.rejectValue("otp","error.otp","C'è stato qualche problema co la richiesta, ricomincia dall'inizio e riprova.");
                    return "ForgotPassword/forgot-password";
                }
                //La nuova password dev'essere diversa dalla vecchia
                if(PasswordUtility.checkPassword(confirmPasswordForm.getNewPassword(),user.getHash_password())) {
                    bindingResult.rejectValue("newPassword","error.newPassword","La nuova password deve essere diversa dalla vecchia.");
                    return "ForgotPassword/forgot-password";
                }
                else { //Se tutto va correttamente, viene modificata la password
                    otpManager.removeOTPEmail(confirmPasswordForm.getEmail());
                    user.setHash_password(PasswordUtility.hashPassword(confirmPasswordForm.getNewPassword()));
                    customUserDetailsService.save(user);
                    return "ForgotPassword/confirm-newPassword";
                }
            }
        }
    }
}