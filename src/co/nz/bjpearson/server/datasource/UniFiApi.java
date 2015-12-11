package co.nz.bjpearson.server.datasource;

import co.nz.bjpearson.server.model.Alert;
import co.nz.bjpearson.server.model.Recording;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniFiApi extends VideoSystem {
    private static final String ALERT_SUFFIX = "api/2.0/alert";
    private static final String RECORDINGS_SUFFIX = "api/2.0/recording";
    private final String API_KEY;
    private final String BASE_URL;

    public UniFiApi(String baseUrl, String apiKey) {
        API_KEY = apiKey;
        BASE_URL = baseUrl;
    }


    private URL buildEndpoint(String suffix, Map<String, String[]> parameters) throws MalformedURLException {
        if(parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put("apiKey", new String[] {API_KEY});
        StringBuilder endpoint = new StringBuilder(BASE_URL + suffix + "?");
        for(Map.Entry<String, String[]> kv : parameters.entrySet()) {
            endpoint.append("&");
            String[] values = kv.getValue();
            String key = "";
            if(values.length > 1) {
                try {
                    key = URLEncoder.encode(kv.getKey() + "[]", "utf-8");
                } catch (UnsupportedEncodingException ignore) {}
            } else {
                try {
                    key = URLEncoder.encode(kv.getKey(), "utf-8");
                } catch (UnsupportedEncodingException ignore) {}
            }
            for(String value : kv.getValue()) {
                endpoint.append(key);
                endpoint.append("&");
                endpoint.append(value);
            }
        }
        return(new URL(endpoint.toString()));
    }



    @Override
    public List<Alert> retrieveAlerts() throws IOException {
        Map<String, String[]> parameters = new HashMap<>();

        parameters.put("archived", new String[] {"false"});


        String endpoint = BASE_URL + ALERT_SUFFIX + "?archived=false&apiKey=" + API_KEY;
        HttpsURLConnection connection = (HttpsURLConnection)(new URL(endpoint).openConnection());
        connection.setDoOutput(true);
        JSONObject resp = (JSONObject) JSONValue.parse(new InputStreamReader(connection.getInputStream()));
        if(!(resp.get("data") instanceof JSONArray)) {
            throw new RuntimeException((String)((JSONObject)resp.get("data")).get("message"));
        }
        JSONArray data = (JSONArray)resp.get("data");
        List<Alert> alerts = new ArrayList<>();
        int i = 0;
        for(Object a : data) {
            alerts.add(Alert.fromJson(i++, (JSONObject)a));
        }
        return alerts;
    }

    @Override
    public List<Recording> retrieveRecordings() throws IOException {
        String endpoint = BASE_URL + RECORDINGS_SUFFIX + "?archived=false&apiKey=" + API_KEY;
        HttpsURLConnection connection = (HttpsURLConnection)(new URL(endpoint).openConnection());
        connection.setDoOutput(true);
        JSONObject resp = (JSONObject) JSONValue.parse(new InputStreamReader(connection.getInputStream()));
        if(!(resp.get("data") instanceof JSONArray)) {
            throw new RuntimeException((String)((JSONObject)resp.get("data")).get("message"));
        }
        JSONArray data = (JSONArray)resp.get("data");
        List<Alert> alerts = new ArrayList<>();
        int i = 0;
        for(Object a : data) {
            alerts.add(Alert.fromJson(i++, (JSONObject)a));
        }
        return null;
    }
}
