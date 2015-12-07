package co.nz.bjpearson.server.model.sms;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class Sms {
    private static final String FROM = "CCTV";

    private String message;
    private Set<String> recipicent;

    public enum MessageStatus {
        SENT ("Sent"),
        SENDING ("Sending"),
        ERROR ("Error");

        private final String status;
        MessageStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
    protected Sms(String message, Set<String> recipicent) {
        this.message =  message;
        this.recipicent = new HashSet<>();
    }

    public abstract void send() throws IOException;
    public abstract MessageStatus getMessageStatus();

}
