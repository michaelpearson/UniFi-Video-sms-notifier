package co.nz.bjpearson.server;

import co.nz.bjpearson.server.datasource.VideoSystem;
import co.nz.bjpearson.server.model.Alert;
import co.nz.bjpearson.server.model.AlertDispatcher;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationWatcher extends Thread {
    private final VideoSystem videoSystem;
    private final AlertDispatcher alertDispatcher;

    private long lastAlertTimestamp;
    private long lastAlertSentAt = 0;

    private int maxQueueSize;
    private int queueDispatchTimeout;

    private Set<Alert> alertQueue = new HashSet<>();

    public NotificationWatcher(VideoSystem videoSystem, AlertDispatcher dispatcher, int maxQueueSize, int queueDispatchTimeout) {
        this.alertDispatcher = dispatcher;
        this.videoSystem = videoSystem;
        this.maxQueueSize = maxQueueSize;
        this.queueDispatchTimeout = queueDispatchTimeout;

        this.lastAlertTimestamp = this.lastAlertSentAt = getTimestamp();
    }

    private static long getTimestamp() {
        return new Date().getTime();
    }

    private void updateAlertQueue() throws IOException {
        List<Alert> alerts = videoSystem.retrieveAlerts();
        long newLastAlert = lastAlertTimestamp;
        for(Alert a : alerts) {
            if(a.getTimestamp() > lastAlertTimestamp) {
                newLastAlert = a.getTimestamp();
                a.debug();
                alertQueue.add(a);
            }
        }
        lastAlertTimestamp = newLastAlert;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                updateAlertQueue();
                dispatchAlerts();
                Thread.sleep(1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchAlerts() throws IOException {
        if((((getTimestamp() - lastAlertSentAt) > queueDispatchTimeout) && alertQueue.size() > 0) || alertQueue.size() > maxQueueSize) {
            lastAlertSentAt = getTimestamp();
            alertDispatcher.sendAlerts(alertQueue).send();
            alertQueue.clear();
        }
    }
}
