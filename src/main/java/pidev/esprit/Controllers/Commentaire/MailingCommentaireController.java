package pidev.esprit.Controllers.Commentaire;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailingCommentaireController {

    @FXML
    private TextField adress_mail;

    @FXML
    private TextField subject_mail;

    @FXML
    private TextArea content_mail;

    @FXML
    private Button mail_btn;

    // Sender's email address (your email)
    private final String myAccountEmail = "omaymasellami2018@gmail.com";

    // Generated app password
    private final String password = "ikel ocib bzut pdfp";

    @FXML
    private void sendemail() {
        String recepient = adress_mail.getText();
        String subject = subject_mail.getText();
        String content = content_mail.getText();

        // Checking if fields are empty
        if (subject.isEmpty() || content.isEmpty()) {
            // Showing alert if fields are empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            if (subject.isEmpty()) {
                alert.setContentText("Please write the subject of the email.");
            } else {
                alert.setContentText("Please write the content of the email.");
            }
            alert.showAndWait();
            return; // Exit the method if fields are empty
        }

        // Sending the email
        try {
            Session session = createSession();
            Message message = preparedMessage(session, myAccountEmail, recepient, subject, content);
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Creating a session with authentication
    private Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });
    }

    // Preparing the message to be sent
    private Message preparedMessage(Session session, String myAccountEmail, String recepient, String subject, String content) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myAccountEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
}
