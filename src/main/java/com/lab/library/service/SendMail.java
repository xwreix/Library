package com.lab.library.service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class SendMail {
    public static void send(String text, String email) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(SendMail.class.getClassLoader().getResourceAsStream("mail.properties"));
        String user = properties.getProperty("mail.smtps.user");
        String pass = properties.getProperty("mail.smtps.password");

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("ka2879365"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Library");
        message.setText(text);

        Transport transport = mailSession.getTransport();
        transport.connect(user, pass);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
