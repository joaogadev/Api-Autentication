package com.api.apiautenticacao.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {
    private final JavaMailSender mailSender;

    public EmailServices(JavaMailSender mailSender) {this.mailSender = mailSender;}

    public void sendEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("apiautenticacao@gmail.com");
        message.setTo(to);
        message.setSubject("Redefinição de Senha!");

        String link = "http://localhost:8080/auth/redefinir-senha?token=" + token;

        message.setText("Olá, você solicitou uma redefinição de senha \n\n" +
                "Clique no link abaixo para redefinir sua senha: \n " +
                link + "Este link expira em 15 minutos!");
        mailSender.send(message);
    }
}
