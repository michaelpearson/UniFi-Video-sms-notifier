package co.nz.bjpearson.server;

import co.nz.bjpearson.server.datasource.UniFiApi;
import co.nz.bjpearson.server.exceptions.InvalidConfigurationException;
import co.nz.bjpearson.server.model.StandardAlert;
import co.nz.bjpearson.server.model.sms.BurstSms;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Main {


    public static void main(String[] argv) {
        File configFile;
        if(argv.length == 0 && (configFile = new File(Configuration.DEFAULT_CONFIG_FILE)).exists()) {
            try {
                Configuration.loadConfiguration(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
                return;
            }
        } else if(argv.length == 1 && (configFile = new File(argv[1])).exists()) {
            try {
                Configuration.loadConfiguration(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
                return;
            }
        } else {
            throw new IllegalArgumentException();
        }

        if(Configuration.UNIFI_CERTIFICATE_FILE != null) {
            addTrustedCertificate(Configuration.UNIFI_CERTIFICATE_FILE);
        }

        try {
            new NotificationWatcher(
                    new UniFiApi(Configuration.UNIFI_BASE_URL, Configuration.UNIFI_API_KEY),
                    new StandardAlert(
                            new BurstSms(
                                    Configuration.BURST_SMS_API_KEY,
                                    Configuration.BURST_SMS_API_SECRET),
                            Configuration.SMS_RECIPIENTS),
                    Configuration.SMS_MAX_ALERTS_IN_QUEUE,
                    Configuration.SMS_QUEUE_DISPATCH_TIMEOUT
            ).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTrustedCertificate(String certificatePath) {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        CertificateFactory certificateFactory;
                        try {
                            certificateFactory = CertificateFactory.getInstance("X.509");
                        } catch (CertificateException e) {
                            throw new RuntimeException();

                        }

                        try {
                            X509Certificate[] certs = new X509Certificate[1];
                            certs[0] = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream(certificatePath));
                            return(certs);
                        } catch (FileNotFoundException | CertificateException e) {
                            e.printStackTrace();
                            return(null);
                        }
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
        HostnameVerifier allHostsValid = (hostname, session) -> {
            System.out.println(hostname);
            return(true);
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
