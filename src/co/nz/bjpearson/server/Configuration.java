package co.nz.bjpearson.server;

import co.nz.bjpearson.server.exceptions.InvalidConfigurationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mpearson on 9/12/2015.
 */
public class Configuration {
    public static final String DEFAULT_CONFIG_FILE = "configuration.json";
    public static String BURST_SMS_API_KEY;
    public static String BURST_SMS_API_SECRET;
    public static String UNIFI_API_KEY;
    public static String UNIFI_BASE_URL;
    public static String UNIFI_CERTIFICATE_FILE;
    public static Set<String> SMS_RECIPIENTS;
    public static int    SMS_MAX_ALERTS_IN_QUEUE;
    public static int SMS_QUEUE_DISPATCH_TIMEOUT;

    public static void loadConfiguration(File configurationFIle) throws InvalidConfigurationException, IOException {
        JSONObject configuration;
        try {
            configuration = (JSONObject)new JSONParser().parse(new FileReader(configurationFIle));
        } catch (ParseException e) {
            throw new InvalidConfigurationException("Invalid configuration syntax");
        }
        BURST_SMS_API_KEY    = (String)((JSONObject)configuration.get("burstSms")).get("apiKey");
        BURST_SMS_API_SECRET = (String)((JSONObject)configuration.get("burstSms")).get("apiSecret");
        UNIFI_BASE_URL       = (String)((JSONObject)configuration.get("uniFiVideo")).get("baseUrl");
        if(!UNIFI_BASE_URL.endsWith("/")) {
            UNIFI_BASE_URL += "/";
        }
        UNIFI_API_KEY        = (String)((JSONObject)configuration.get("uniFiVideo")).get("apiKey");

        SMS_RECIPIENTS = new HashSet<>();
        JSONArray r = (JSONArray)((JSONObject)configuration.get("alerts")).get("recipients");
        for(Object recipient : r) {
            SMS_RECIPIENTS.add((String)recipient);
        }
        UNIFI_CERTIFICATE_FILE = (String)((JSONObject)configuration.get("uniFiVideo")).get("httpCertificateFile");

        SMS_MAX_ALERTS_IN_QUEUE = (int)((long)((JSONObject)configuration.get("alerts")).get("maxInQueue"));
        SMS_QUEUE_DISPATCH_TIMEOUT = (int)((long)((JSONObject)configuration.get("alerts")).get("queueTimeout")) * 1000;
    }
}
