package co.nz.bjpearson.server.datasource;


import co.nz.bjpearson.server.model.Alert;
import co.nz.bjpearson.server.model.Recording;

import java.io.IOException;
import java.util.List;

public abstract class VideoSystem {
    public abstract List<Alert> retrieveAlerts() throws IOException;
    public abstract List<Recording> retrieveRecordings() throws IOException;
}
