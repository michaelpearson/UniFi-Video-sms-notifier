package co.nz.bjpearson.server.datasource;


import co.nz.bjpearson.server.model.Alert;

import java.io.IOException;
import java.util.List;

public abstract class AlertProvider {
    public abstract List<Alert> retrieveAlerts() throws IOException;
}
