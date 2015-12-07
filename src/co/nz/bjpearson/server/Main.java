package co.nz.bjpearson.server;

/**
 * Created by mpearson on 7/12/2015.
 */
public class Main {
    private static final String ENDPOINT = "https://server.bjpearson.co.nz/";
    public static void main(String[] argv) {
        String endpoint;
        if(argv.length > 0) {
            endpoint = argv[0];
        } else {
            endpoint = ENDPOINT;
        }

    }
}
