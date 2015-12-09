package co.nz.bjpearson.server.test;

import co.nz.bjpearson.server.Configuration;
import co.nz.bjpearson.server.exceptions.InvalidConfigurationException;
import co.nz.bjpearson.server.model.sms.BurstSms;
import co.nz.bjpearson.server.model.sms.Sms;
import co.nz.bjpearson.server.model.sms.SmsBalance;
import co.nz.bjpearson.server.model.sms.SmsFactory;

import java.io.File;
import java.io.IOException;

public class SmsTest {
    public static void main(String[] argv) throws IOException {
        File configFile;
        configFile = new File(Configuration.DEFAULT_CONFIG_FILE);
        try {
            Configuration.loadConfiguration(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }
        testBalance();
        testSend();
        testBalance();
    }

    private static void testBalance() throws IOException {
        BurstSms burstSms = new BurstSms(Configuration.BURST_SMS_API_KEY, Configuration.BURST_SMS_API_SECRET);
        SmsBalance balance = burstSms.getBalance();
        System.out.println(String.format("Balance: $%2.2f %s", balance.getDollarValue(), balance.getCurrency()));
    }
    private static void testSend() throws IOException {
        SmsFactory burstSmsFactory = new BurstSms(Configuration.BURST_SMS_API_KEY, Configuration.BURST_SMS_API_SECRET);
        Sms sms = burstSmsFactory.createSms("This is a test sms message!!!", Configuration.SMS_RECIPIENTS);
        sms.send();
    }
}
