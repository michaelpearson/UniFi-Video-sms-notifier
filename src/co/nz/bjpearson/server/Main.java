package co.nz.bjpearson.server;

import co.nz.bjpearson.server.controller.Watcher;
import co.nz.bjpearson.server.datasource.UniFiApi;
import co.nz.bjpearson.server.model.sms.implementation.BurstSmsFactory;

public class Main {
    public static void main(String[] argv) {
        if(argv.length < 3) {
            throw new IllegalArgumentException("Usage: Endpoint ApiKey");
        }
        try {
            new Watcher(new BurstSmsFactory(), new UniFiApi(argv[0], argv[1])).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
