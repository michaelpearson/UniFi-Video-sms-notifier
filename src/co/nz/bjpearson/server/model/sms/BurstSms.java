package co.nz.bjpearson.server.model.sms;

import co.nz.bjpearson.server.model.sms.Sms;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class BurstSms extends SmsFactory {

    private final String apiKey;
    private final String apiSecret;

    private static final URL SEND_ENDPOINT;
    private static final URL STATUS_ENDPOINT;

    static {
        try {
            SEND_ENDPOINT = new URL("http://api.transmitsms.com/send-sms.json");
            STATUS_ENDPOINT = new URL("http://api.transmitsms.com/get-sms-stats.json");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid url.");
        }
    }


    public BurstSms(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public Sms createSms(final String message, final Set<String> recipients) {
        return new Sms(message, recipients) {
            private long messageId = -1;

            @Override
            public void send() throws IOException {
                System.out.println(String.format("Send sms! first recipient: %s sms body: %s", recipients.toArray()[0], message));
                //HttpsURLConnection connection = (HttpsURLConnection) SEND_ENDPOINT.openConnection();
                //connection.setDoInput(true);
            }

            @Override
            public Sms.MessageStatus getMessageStatus() {
                if (messageId == -1) {
                    throw new IllegalStateException("Message not sent");
                }
                return Sms.MessageStatus.SENT;
            }
        };
    }
}
