package stockalerts.email;

import org.springframework.stereotype.Component;
import stockalerts.utils.Recipients;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailClient {

    public void sendEmail(String subject, String body, Recipients recipients) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        final String username = "johnnysPS5Updates@gmail.com";
        final String password = "Monkeydust5";

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipients.getEmails())
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Sent emails");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
