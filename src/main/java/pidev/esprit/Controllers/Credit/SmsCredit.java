package pidev.esprit.Controllers.Credit;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsCredit {
    public static final String ACCOUNT_SID = "AC63a111f87e3f41a06a7a57a3cb709c52";
    public static final String AUTH_TOKEN = "b7b196ecca455cd72e0b6606db3f49c1";
    public static final String FROM_PHONE_NUMBER = "+19145955551";

    public static void sendSMS(String toPhoneNumber, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber(toPhoneNumber),  // Numéro de téléphone du destinataire
                        new PhoneNumber(FROM_PHONE_NUMBER),  // Votre numéro Twilio
                        messageBody)
                .create();

        System.out.println("Message SID: " + message.getSid());
    }
}
