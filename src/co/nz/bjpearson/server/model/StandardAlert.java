package co.nz.bjpearson.server.model;

import co.nz.bjpearson.server.model.sms.Sms;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import java.util.Set;

/**
 * Created by mpearson on 8/12/2015.
 */
public class StandardAlert extends AlertDispatcher {

    public StandardAlert(SmsFactory smsFactory, Set<String> recipients) {
        super(smsFactory, recipients);
    }

    @Override
    public Sms sendAlerts(Set<Recording> alerts) {
        StringBuilder alertMessage = new StringBuilder();
        int i = 1;
        for(Recording r : alerts) {
            alertMessage.append(String.format("Alert %d: %s\n", i++, r.getStartTime().toString()));
            alertMessage.append(String.format("Zone: %s\n", r.getNotes()));
        }
        return smsFactory.createSms(alertMessage.toString(), recipients);
    }
}
