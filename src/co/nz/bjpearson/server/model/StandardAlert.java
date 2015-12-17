package co.nz.bjpearson.server.model;

import co.nz.bjpearson.server.model.sms.Sms;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.logging.SimpleFormatter;

public class StandardAlert extends AlertDispatcher {

    public StandardAlert(SmsFactory smsFactory, Set<String> recipients) {
        super(smsFactory, recipients);
    }

    @Override
    public Sms sendAlerts(Set<Recording> alerts) {
        StringBuilder alertMessage = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        for(Recording r : alerts) {
            alertMessage.append(String.format("Movement detected at %s ", formatter.format(r.getStartTime())));
            boolean isFullTime = r.getNotes() == null || r.getNotes().equals("");
            alertMessage.append(String.format("by the %s %s\n", isFullTime ? r.getCameraName() : r.getNotes(), isFullTime ? "camera" : "zone"));
        }
        return smsFactory.createSms(alertMessage.toString(), recipients);
    }
}
