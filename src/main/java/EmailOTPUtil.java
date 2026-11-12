import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailOTPUtil {
    private static final String SENDER_EMAIL = System.getenv("SMTP_EMAIL") != null 
            ? System.getenv("SMTP_EMAIL") : "your-school-email@gmail.com";
    private static final String APP_PASSWORD = System.getenv("SMTP_PASS") != null 
            ? System.getenv("SMTP_PASS") : "your-16-char-gmail-app-password";

    public static String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static boolean sendEmail(String recipient, String subject, String bodyText) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "Smart School PRO"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(bodyText);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
