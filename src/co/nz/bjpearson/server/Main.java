package co.nz.bjpearson.server;

import co.nz.bjpearson.server.datasource.UniFiApi;
import co.nz.bjpearson.server.model.StandardAlert;
import co.nz.bjpearson.server.model.sms.BurstSms;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final String DEFAULT_CONFIG = "configuration.json";

    static {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

                }
        };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }


    public static void main(String[] argv) {
        String smsApiKey, smsApiSecret, unifiBaseUrl, unifiApiKey;
        Set<String> recipients = new HashSet<>();
        File configFile;
        if(argv.length == 4) {
            smsApiKey = argv[0];
            smsApiSecret = argv[1];
            unifiBaseUrl = argv[2];
            unifiApiKey = argv[3];
        } else if(argv.length == 0 && (configFile = new File(DEFAULT_CONFIG)).exists()) {
            try {
                JSONObject configuration = (JSONObject)new JSONParser().parse(new FileReader(configFile));
                smsApiKey = (String)((JSONObject)configuration.get("burstSms")).get("apiKey");
                smsApiSecret = (String)((JSONObject)configuration.get("burstSms")).get("apiSecret");
                unifiBaseUrl = (String)((JSONObject)configuration.get("uniFiVideo")).get("baseUrl");
                unifiApiKey = (String)((JSONObject)configuration.get("uniFiVideo")).get("apiKey");
                JSONArray r = (JSONArray)((JSONObject)configuration.get("alerts")).get("recipients");
                for(Object recipient : r) {
                    recipients.add((String)recipient);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return;
            }
        } else {
            throw new IllegalArgumentException();
        }

        if(!unifiBaseUrl.endsWith("/")) {
            unifiBaseUrl += "/";
        }
        try {
            new NotificationWatcher(new UniFiApi(unifiBaseUrl, unifiApiKey), new StandardAlert(new BurstSms(smsApiKey, smsApiSecret), recipients)).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
