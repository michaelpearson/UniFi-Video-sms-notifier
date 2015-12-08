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
    public Sms sendAlerts(Set<Alert> alerts) {
        StringBuilder alertMessage = new StringBuilder();
        int i = 0;
        for(Alert a : alerts) {
            alertMessage.append(String.format("Alert %d received at: ", ++i));
            alertMessage.append(a.getDate().toString());
            alertMessage.append("\n");
            alertMessage.append("Alert type: ");
            alertMessage.append(a.getType());
            alertMessage.append("\n");
        }
        return smsFactory.createSms(alertMessage.toString(), recipients);
    }
}
