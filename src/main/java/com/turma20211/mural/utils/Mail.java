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
    private final String USERNAME = "";
    private final String PASSWORD = "";
    private final String EMAIL = "@gmail.com";

    private final boolean AUTH = true;
    private final boolean STARTTLS = true;

    public void send(User user) throws AddressException, MessagingException, IOException {

        Session session = Session.getDefaultInstance(setProperties());

        Message msg = new MimeMessage(setSession(setProperties()));

        msg.setSentDate(new Date());
        msg.setSubject("Cadastro no Mural da Turma");

        msg.setFrom(new InternetAddress(EMAIL, false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));

        msg.setContent(user.getFirstName().concat(", bem-vindo(a) ao Mural da Turma!"), "text/html");

        Transport transport = session.getTransport("smtp");

        Transport.send(msg);
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

