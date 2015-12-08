package co.nz.bjpearson.server;

import co.nz.bjpearson.server.datasource.AlertProvider;
import co.nz.bjpearson.server.model.Alert;
import co.nz.bjpearson.server.model.AlertDispatcher;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationWatcher extends Thread {
    private final AlertProvider alertProvider;
    private final AlertDispatcher alertDispatcher;

    private long lastAlertTimestamp = 0;
    private long lastAlertSentAt = 0;

    private static final int MAX_QUEUE_SIZE = 5;
    private static final int QUEUE_DISPATCH_TIMEOUT = 20; //20 seconds

    private Set<Alert> alertQueue = new HashSet<>();

    public NotificationWatcher(AlertProvider alertProvider, AlertDispatcher dispatcher) {
        this.alertDispatcher = dispatcher;
        this.alertProvider = alertProvider;
    }

    private static long getTimestamp() {
        return new Date().getTime();
    }

    private void updateAlertQueue() throws IOException {
        List<Alert> alerts = alertProvider.retrieveAlerts();
        long newLastAlert = lastAlertTimestamp;
        for(Alert a : alerts) {
            if(a.getTimestamp() > lastAlertTimestamp) {
                newLastAlert = a.getTimestamp();
                alertQueue.add(a);
            }
            a.debug();
        }
        lastAlertTimestamp = newLastAlert;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                updateAlertQueue();
                if(alertQueue.size() > 0) {
                    dispatchAlerts();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchAlerts() throws IOException {
        if(lastAlertSentAt == 0 || lastAlertTimestamp == 0) {
            lastAlertSentAt = lastAlertTimestamp = getTimestamp();
        }
        if(getTimestamp() - lastAlertSentAt > QUEUE_DISPATCH_TIMEOUT || alertQueue.size() > MAX_QUEUE_SIZE) {
            lastAlertSentAt = getTimestamp();
            alertDispatcher.sendAlerts(alertQueue).send();
            alertQueue.clear();
        }
    }
}
