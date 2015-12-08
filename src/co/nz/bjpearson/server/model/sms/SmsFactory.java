package co.nz.bjpearson.server.model.sms;

import java.util.Set;

/**
 * Created by mpearson on 7/12/2015.
 */
public abstract class SmsFactory {

    public abstract Sms createSms(String message, Set<String> recipients);

}
