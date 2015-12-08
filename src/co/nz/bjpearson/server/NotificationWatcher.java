package co.nz.bjpearson.server;

import co.nz.bjpearson.server.datasource.AlertProvider;
import co.nz.bjpearson.server.model.Alert;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import java.io.IOException;
import java.util.List;

public class NotificationWatcher extends Thread {
    private final SmsFactory smsFactory;
    private final AlertProvider alertProvider;
    private long lastAlertTimestamp = -1;

    public NotificationWatcher(SmsFactory service, AlertProvider alertProvider) {
        this.smsFactory = service;
        this.alertProvider = alertProvider;
    }


    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                List<Alert> alerts = alertProvider.retrieveAlerts();
                for(Alert a : alerts) {
                    a.debug();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
