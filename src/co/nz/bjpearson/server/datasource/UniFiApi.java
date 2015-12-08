package co.nz.bjpearson.server.datasource;

import co.nz.bjpearson.server.model.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UniFiApi extends AlertProvider {
    private static final String ALERT_SUFFIX = "api/2.0/alert";
    private final String API_KEY;
    private final String BASE_URL;

    public UniFiApi(String baseUrl, String apiKey) {
        API_KEY = apiKey;
        BASE_URL = baseUrl;
    }

    @Override
    public List<Alert> retrieveAlerts() throws IOException {
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
}
