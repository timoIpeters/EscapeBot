package logic.exceptions;

/**
 * Custom exception which will be thrown if the loaded level file has no "field" property or if the property is null.
 *
 * @author Timo Peters
 */
public class NoFieldException extends Exception {
    public NoFieldException() {
        super("NoFieldException: field property was null");
    }
}
