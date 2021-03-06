package co.nz.bjpearson.server.exceptions;

/**
 * Created by mpearson on 9/12/2015.
 */
public class InvalidConfigurationException extends Exception {
    public InvalidConfigurationException() {}

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
