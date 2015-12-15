package co.nz.bjpearson.server.model;

import co.nz.bjpearson.server.model.sms.Sms;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import java.util.Set;

public abstract class AlertDispatcher {
    protected SmsFactory smsFactory;
    protected Set<String> recipients;

    public AlertDispatcher(SmsFactory smsFactory, Set<String> recipients) {
        this.smsFactory = smsFactory;
        this.recipients = recipients;
    }

    public abstract Sms sendAlerts(Set<Recording> alerts);
}
