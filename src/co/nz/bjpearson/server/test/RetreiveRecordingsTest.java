package co.nz.bjpearson.server.test;

import co.nz.bjpearson.server.Configuration;
import co.nz.bjpearson.server.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * Created by michael on 10/12/15.
 */
public class RetreiveRecordingsTest {

    public static void main(String[] argv) throws IOException, InvalidConfigurationException {
        Configuration.loadConfiguration(new File("cert.cer"));


    }

}
