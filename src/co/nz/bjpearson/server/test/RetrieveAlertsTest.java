package co.nz.bjpearson.server.test;

import co.nz.bjpearson.server.Configuration;
import co.nz.bjpearson.server.Main;
import co.nz.bjpearson.server.datasource.UniFiApi;
import co.nz.bjpearson.server.exceptions.InvalidConfigurationException;
import co.nz.bjpearson.server.model.Alert;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.io.File;
import java.io.IOException;

/**
 * Created by michael on 10/12/15.
 */
public class RetrieveAlertsTest {

    public static void main(String[] argv) throws IOException, InvalidConfigurationException {
        Configuration.loadConfiguration(new File("configuration.json"));
        Main.addTrustedCertificate(Configuration.UNIFI_CERTIFICATE_FILE);
        UniFiApi uniFiApi = new UniFiApi(Configuration.UNIFI_BASE_URL, Configuration.UNIFI_API_KEY);

        for(Alert a : uniFiApi.retrieveAlerts()) {
            a.debug();
        }
    }

}
