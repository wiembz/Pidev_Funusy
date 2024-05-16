package pidev.esprit.Controllers.Account;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {

    public static final String ACCOUNT_SID = "ACf88da21b5dd341dfc3487e999751b2c0";
    public static final String AUTH_TOKEN = "655035033bf00a336d320b3188e69127";

    // Method to send SMS message
    public static void sendSMS(String to, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Send SMS message using Twilio API
        Message message = Message.creator(new PhoneNumber(to), new PhoneNumber("+12603773033"),body).create();

        // Print the message SID for reference
        System.out.println("SMS sent successfully. Message SID: " + message.getSid());
    }
}
