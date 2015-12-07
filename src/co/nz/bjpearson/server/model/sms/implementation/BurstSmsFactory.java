package co.nz.bjpearson.server.model.sms.implementation;

import co.nz.bjpearson.server.model.sms.Sms;
import co.nz.bjpearson.server.model.sms.SmsFactory;

public class BurstSmsFactory extends SmsFactory {

    private final String apiKey;
    private final String apiSecret;

    public BurstSmsFactory(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public Sms createSms() {
        return null;
    }
}
