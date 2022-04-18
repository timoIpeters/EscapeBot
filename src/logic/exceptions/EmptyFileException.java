package logic.exceptions;

/**
 * Custom exception which will be thrown if a selected level file is empty.
 *
 * @author Timo Peters
 */
public class EmptyFileException extends Exception {
    public EmptyFileException() {
        super("EmptyFileException: The file is empty");
    }
}
