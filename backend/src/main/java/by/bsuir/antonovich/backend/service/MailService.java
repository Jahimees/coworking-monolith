package by.bsuir.antonovich.backend.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    public static final boolean AUTH = true;
    public static final boolean START_TTLS = true;
    public static final String HOST = "smtp.mail.ru";
    public static final String PORT = "587"; //587
    public static final boolean SSL_TRUST = true;


    public void sendEmail(String messageBody, String messageTitle, String receiver) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.auth", AUTH);
        properties.put("mail.smtp.ssl.trust", SSL_TRUST);
        properties.put("mail.smtp.starttls.enable", START_TTLS);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("antonovich.01@mail.ru", "rEFpYx47cGnGuQepV4Mz");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("antonovich.01@mail.ru"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(receiver));
        message.setSubject(messageTitle);


        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(messageBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}

