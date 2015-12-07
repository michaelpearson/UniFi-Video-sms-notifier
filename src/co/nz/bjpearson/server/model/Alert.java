package co.nz.bjpearson.server.model;

import org.json.simple.JSONObject;

import java.util.Date;

public class Alert {
    private final int index;
    private final String id;
    private final String admin;
    private final String alertType;
    private final boolean archived;
    private final String type;
    private final long timestamp;
    private final Date date;

    private Alert(int index, String id, String admin, String alertType, boolean archived, String type, long timestamp) {
        this.index = index;
        this.id = id;
        this.admin = admin;
        this.alertType = alertType;
        this.archived = archived;
        this.type = type;
        this.timestamp = timestamp;
        this.date = new Date(timestamp);
    }

    public static Alert fromJson(int index, JSONObject obj) {
        return new Alert(
                index,
                (String)obj.get("_id"),
                (String)obj.get("admin"),
                (String)obj.get("alertType"),
                (Boolean)obj.get("archived"),
                (String)obj.get("type"),
                (long)obj.get("timestamp")
        );
    }


    public void debug() {
        log(String.format("%d: %s\t%s", index, id, date.toString()));
    }

    private void log(String message) {
        System.out.println(message);
    }
}
