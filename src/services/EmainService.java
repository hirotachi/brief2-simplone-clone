package services;

import models.SendEnhancedRequestBody;
import models.SendEnhancedResponseBody;
import models.SendRequestMessage;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.io.IOException;
import java.util.HashMap;

import static org.simplejavamail.mailer.MailerBuilder.buildMailer;

public class EmainService {
    public static void send(String to, String subject, String body) {
        Courier.init("pk_prod_73M6FWXENX4ZT2JD9FMFKNF1G13A");

        SendEnhancedRequestBody sendEnhancedRequestBody = new SendEnhancedRequestBody();
        SendRequestMessage sendRequestMessage = new SendRequestMessage();
        HashMap<String, String> destinations = new HashMap<String, String>();
        destinations.put("email", to);
        sendRequestMessage.setTo(destinations);

        HashMap<String, String> content = new HashMap<String, String>();
        content.put("title", subject);
        content.put("body", body);
        sendRequestMessage.setContent(content);
        sendEnhancedRequestBody.setMessage(sendRequestMessage);

        try {
            SendEnhancedResponseBody response = new SendService().sendEnhancedMessage(sendEnhancedRequestBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
