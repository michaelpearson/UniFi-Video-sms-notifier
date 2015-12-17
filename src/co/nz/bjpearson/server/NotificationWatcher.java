package co.nz.bjpearson.server;

import co.nz.bjpearson.server.datasource.VideoSystem;
import co.nz.bjpearson.server.model.Alert;
import co.nz.bjpearson.server.model.AlertDispatcher;
import co.nz.bjpearson.server.model.Recording;

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

    private Set<Recording> alertQueue = new HashSet<>();

    private static final int MINIMUM_LENGTH = 24 * 1000;

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
        List<Recording> recordings = videoSystem.retrieveRecordings();
        System.out.println(String.format("Got %d recordings", recordings.size()));
        long newLastAlert = lastAlertTimestamp;
        for(Recording r : recordings) {
            if(r.getStartTime().getTime() > lastAlertTimestamp) {
                if(r.isInProgress()) {
                    System.out.println("Recording in progress, ignoring");
                    continue;
                }
                newLastAlert = r.getStartTime().getTime();
                if(r.getLength() > MINIMUM_LENGTH) {
                    alertQueue.add(r);
                } else {
                    System.out.print("Did not add recording due to insufficient length: ");
                }
                r.debug();
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
                Thread.sleep(10000);
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
