package co.nz.bjpearson.server.model.sms;

import org.json.simple.JSONObject;

/**
 * Created by mpearson on 9/12/2015.
 */
public class SmsBalance {
    private double balance;
    private String currency;

    private SmsBalance(double balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public static SmsBalance fromJson(JSONObject balance) {
        return(new SmsBalance((double)balance.get("balance"), (String)balance.get("currency")));
    }

    public double getDollarValue() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}
