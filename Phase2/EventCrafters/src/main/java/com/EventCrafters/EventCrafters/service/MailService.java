package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    private final String username;
    private final String password;
    private final Session session;

    public MailService(){
        username = "marquesgarciaangel@gmail.com";
        password = "zdag mpol eeyf bbnf";
        //marquesgarciaangel zdag mpol eeyf bbnf
        //eventcraftersurjc gcun zfgb fdds uuqe

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public String sendEmail(User recipient, String subject, String content, boolean isHtml){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient.getEmail())
            );
            message.setSubject(subject);
            if (!isHtml){
                message.setText(content);
            } else {
                message.setContent(content, "text/html");
            }

            Transport.send(message);
            System.out.println(content);

            return "emailSent";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "error";
        }
    }

}
