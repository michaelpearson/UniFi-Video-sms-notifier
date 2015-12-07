package co.nz.bjpearson.server.datasource;

import model.Alert;

import java.util.Enumeration;

/**
 * Created by mpearson on 7/12/2015.
 */
public abstract class AlertProvider {
    public abstract Enumeration<Alert> retreiveAlerts();
}
