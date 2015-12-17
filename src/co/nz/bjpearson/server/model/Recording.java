package co.nz.bjpearson.server.model;

import co.nz.bjpearson.server.Configuration;
import co.nz.bjpearson.server.datasource.UniFiApi;
import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class Recording {
    private String id;
    private boolean archived;
    private String cameraName;
    private String cameraUuid;
    private String cause;
    private Date endTime;
    private boolean inProgress;
    private String notes;
    private long length;
    private String serverUuid;
    private Date startTime;
    private String status;



    private Recording(String id, boolean archived, String cameraName, String cameraUuid, String cause, long endTime, boolean inProgress, String notes, long length, String serverUuid, long startTime, String status) {
        this.id = id;
        this.archived = archived;
        this.cameraName = cameraName;
        this.cameraUuid = cameraUuid;
        this.cause = cause;
        this.endTime = new Date(endTime);
        this.inProgress = inProgress;
        this.notes = notes;
        this.length = length;
        this.serverUuid = serverUuid;
        this.startTime = new Date(startTime);
        this.status = status;
    }

    public static Recording fromJson(int index, JSONObject obj) {
        return new Recording(
                (String)obj.get("_id"),
                (boolean)obj.get("archived"),
                (String)obj.get("cameraName"),
                (String)obj.get("cameraUuid"),
                (String)obj.get("cause"),
                (long)obj.get("endTime"),
                (boolean)obj.get("inProgress"),
                (String)obj.get("notes"),
                (long)obj.get("playLength"),
                (String)obj.get("serverUuid"),
                (long)obj.get("startTime"),
                (String)obj.get("status")
        );
    }


    public void debug() {
        System.out.println(String.format("Recording: %s, notes: %s, date time: %s, length: %d, %s",
                id,
                notes,
                startTime.toString(),
                length / 1000,
                getSnapshotUrl()));
    }

    public String getId() {
        return id;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getCameraName() {
        return cameraName;
    }

    public String getCameraUuid() {
        return cameraUuid;
    }

    public String getCause() {
        return cause;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public String getNotes() {
        return notes;
    }

    public long getLength() {
        return length;
    }

    public String getServerUuid() {
        return serverUuid;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getStatus() {
        return status;
    }

    public String getSnapshotUrl() {
        return(getSnapshotUrl(false));
    }

    public String getSnapshotUrl(boolean thumbnail) {
        return(getSnapshotUrl(Configuration.UNIFI_BASE_URL, thumbnail));
    }

    public String getSnapshotUrl(String unifiBaseUrl, boolean thumbnail) {
        Calendar c = Calendar.getInstance();
        c.setTime(getStartTime());
        return(String.format("%sapi/2.0/snapshot/recording/%s/%d/%d/%d/%s",
                unifiBaseUrl,
                getCameraUuid(),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                getId()
                ) + (thumbnail ? "thumb=true" : ""));
    }
}
