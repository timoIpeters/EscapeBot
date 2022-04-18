package logic.exceptions;

/**
 * Custom exception which will be thrown if the loaded level file has no "botRotation" property or if the property is null.
 *
 * @author Timo Peters
 */
public class NoBotRotationException extends Exception {
    public NoBotRotationException() {
        super("NoBotRotationException: botRotation property was null");
    }
}
