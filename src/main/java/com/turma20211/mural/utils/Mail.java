package com.turma20211.mural.utils;

import com.turma20211.mural.model.User;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Mail {
    private final String PORT = "587";
    private final String HOST = "smtp.gmail.com";
    private final String USERNAME = "muraldaturma";
    private static String PASSWORD = System.getenv("EMAIL_PASSWORD_MURAL");
    private final String EMAIL = "muraldaturma@gmail.com";

    private final boolean AUTH = true;
    private final boolean STARTTLS = true;

    public void sendEmail(User user, String url, String subject, String content) throws AddressException, MessagingException, IOException {

        Session session = Session.getDefaultInstance(setProperties());

        Message msg = new MimeMessage(setSession(setProperties()));

        msg.setSentDate(new Date());
        msg.setSubject(subject);

        msg.setFrom(new InternetAddress(EMAIL, true));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));

        url = "<a href="+url+" target='_blank'>Clique Aqui</a>";

        msg.setContent(content, "text/html");

        Transport transport = session.getTransport("smtp");

        Transport.send(msg);
    }

    public void sendConfirmationAccount(User user, String url) throws AddressException, MessagingException, IOException {
        String subject = "Cadastro no Mural da Turma";
        String content = user.getFirstName().concat(", bem-vindo(a) ao Mural da Turma!" +
                "\nAcesse o link abaixo para confirmar sua conta.").concat(url);
        this.sendEmail(user, url, subject, content);
    }

    public void sendRecoveryEmail(User user, String url) throws MessagingException, IOException {
        String subject = "Recuperação de Senha - " + user.getFirstName() + " " + user.getLastName();
        String content = "Olá,  ".concat(user.getFirstName()).concat(", você solicitou recuperação de senha para sua conta com nome de usuário ").concat(user.getUsername()).concat(". Acesse o link abaixo para alterar sua sua senha.\n ").concat(url);
        this.sendEmail(user, url, subject, content);
    }

    private Session setSession(Properties props) {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        return session;
    }

    private Properties setProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.clave", PASSWORD);
        props.put("mail.smtp.user",USERNAME);
        props.put("mail.smtp.starttls.enable", STARTTLS);

        return props;
    }
}

