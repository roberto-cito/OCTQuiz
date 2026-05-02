package com.oct.octquiz.Model.Support;

import com.oct.octquiz.Model.Email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SupportService {

    private final SupportRepository supportRepository;
    private final EmailService emailService;
    private final String senderEmail;
    private final String adminEmail;
    private final List<String> categories=new ArrayList<>(List.of("Domande", "Suggerimenti", "Bug", "Altro"));

    public SupportService(SupportRepository supportRepository, EmailService emailService, @Value("${spring.mail.username}") String senderEmail, @Value("${admin.mail}") String adminEmail) {
        this.supportRepository = supportRepository;
        this.emailService = emailService;
        this.senderEmail = senderEmail;
        this.adminEmail = adminEmail;
    }

    public List<SupportEntity> getAllMessages() {
        return supportRepository.findAll(Sort.by(Sort.Direction.DESC,"data"));
    }

    public void removeMessage(int id) {
        supportRepository.deleteById(id);
    }

    public void removeAllMessages() {
        supportRepository.deleteAll();
    }

    public void addNewMessage(String email, String messaggio, String categoria) {
        Date date = new Date();
        SupportEntity message = new SupportEntity(email, categoria, messaggio, date);
        supportRepository.save(message);
        supportRepository.flush();
        SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance();
        String formattedDate = sdf.format(date);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(senderEmail);
        mail.setTo("octquiz@olimpiadidellacultura.it");
        mail.setCc(adminEmail);
        mail.setSubject("Messaggio da "+email+" il "+formattedDate+" per la categoria "+message.getCategoria());
        mail.setText(message.getMessaggio());
        emailService.sendEmail(mail);
    }

    public List<String> getCategories() {
        return categories;
    }

    public int getNumberOfMessages() {
        return supportRepository.findAll().size();
    }
}
