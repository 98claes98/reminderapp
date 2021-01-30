package se.claesandersson.reminderapp.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Claes Andersson
 */
public class SendMail {

    public static void sendMail(String to, String subject, String message) {

        // Sender's email ID needs to be mentioned
        String from = System.getenv("EMAIL_FROM");

        // Get system properties
        Properties properties = new Properties();

        // Setup mail server
        properties.put("mail.smtp.host", System.getenv("EMAIL_HOST"));
        properties.put("mail.smtp.port", System.getenv("EMAIL_PORT"));
        properties.put("mail.smtp.auth", System.getenv("EMAIL_AUTH"));
        properties.put("mail.smtp.starttls.enable", System.getenv("EMAIL_SSL"));

        Session session;
        if (System.getenv("EMAIL_AUTH").toLowerCase().equals("true")) {
            session = Session.getInstance(properties, new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(System.getenv("EMAIL_USER"), System.getenv("EMAIL_PASSWORD"));
                }
            });
        } else {
            session = Session.getInstance(properties);
        }

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage mail = new MimeMessage(session);

            // Set From: header field of the header.
            mail.setFrom(new InternetAddress(from, "reminderapp"));

            // Set To: header field of the header.
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            mail.setSubject(subject);

            // Now set the actual message
            mail.setText(message);

            Transport.send(mail);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
