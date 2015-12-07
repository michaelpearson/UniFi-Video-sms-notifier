package co.nz.bjpearson.server.model.sms.implementation;

import co.nz.bjpearson.server.model.sms.Sms;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class BurstSms extends Sms {

    private static final URL SEND_ENDPOINT;
    private static final URL STATUS_ENDPOINT;

    static {
        try {
            SEND_ENDPOINT = new URL("http://api.transmitsms.com/send-sms.json");
            STATUS_ENDPOINT = new URL("http://api.transmitsms.com/get-sms-stats.json");
        } catch(MalformedURLException ignore) {
            throw new RuntimeException("Invalid endpoints");
        }
    }

    private long messageId = -1;

    public BurstSms(String message, Set<String> recipicent) {
        super(message, recipicent);
    }

    @Override
    public void send() throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection)SEND_ENDPOINT.openConnection();
        connection.setDoInput(true);

    }

    @Override
    public MessageStatus getMessageStatus() {
        if(messageId == -1) {
            throw new IllegalStateException("Message not sent");
        }
        return MessageStatus.SENT;
    }
}
