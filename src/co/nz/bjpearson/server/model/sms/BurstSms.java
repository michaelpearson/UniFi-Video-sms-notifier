package co.nz.bjpearson.server.model.sms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Set;

public class BurstSms extends SmsFactory {

    private static final boolean LIVE = true;

    private final String apiKey;
    private final String apiSecret;
    private long messageId = 0;

    private static final URL SEND_ENDPOINT;
    private static final URL BALANCE_ENDPOINT;
    private static final URL STATUS_ENDPOINT;

    static {
        try {
            SEND_ENDPOINT = new URL("https://api.transmitsms.com/send-sms.json");
            STATUS_ENDPOINT = new URL("https://api.transmitsms.com/get-sms-stats.json");
            BALANCE_ENDPOINT = new URL("https://api.transmitsms.com/get-balance.json");
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
                System.out.println(String.format("Begin SMS\n-------------------------\n%s\n-------------------------\n", message));
                if(!LIVE) {
                    return;
                }
                HttpsURLConnection connection = getConnection(SEND_ENDPOINT);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                StringBuilder recipientData = new StringBuilder();
                for(String r : recipients) {
                    recipientData.append(r);

                }
                String data = String.format("message=%s&to=%s", URLEncoder.encode(message, "utf-8"), URLEncoder.encode(String.join(",", recipients), "utf-8"));
                connection.getOutputStream().write(data.getBytes());
                JSONObject response;
                try {
                    response = (JSONObject)new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
                    //System.out.println(response.toJSONString());
                } catch(ParseException e) {
                    throw new RuntimeException("Error parsing response");
                }
                this.messageId = (long)response.get("message_id");
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

    public HttpsURLConnection getConnection(URL endpoint) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection)endpoint.openConnection();
        String userCredentials = String.format("%s:%s", apiKey, apiSecret);
        connection.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes())));
        connection.setDoInput(true);
        return(connection);
    }

    public SmsBalance getBalance() throws IOException {
        HttpsURLConnection connection = getConnection(BALANCE_ENDPOINT);
        try {
            return SmsBalance.fromJson((JSONObject)new JSONParser().parse(new InputStreamReader(connection.getInputStream())));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid response");
        }
    }

    public long getMessageId() {
        return messageId;
    }
}
