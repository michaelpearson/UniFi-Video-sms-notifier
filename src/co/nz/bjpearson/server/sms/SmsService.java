package co.nz.bjpearson.server.sms;

/**
 * Created by mpearson on 7/12/2015.
 */
public abstract class SmsService {
    protected String body;
    protected String recipient;

    public abstract boolean sendSms();
    public abstract boolean chechSmsStatus();
    public abstract void newSms();
    public abstract int getRemainingBalance();
}
