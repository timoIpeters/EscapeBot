package logic.exceptions;

/**
 * Custom exception which will be thrown if the field size is invalid.
 * A field is only valid if it has a size of 8x8.
 *
 * @author Timo Peters
 */
public class InvalidFieldSizeException extends Exception {
    public InvalidFieldSizeException(int[][] field, int col) {
        super("InvalidFieldSizeException: Expected 8x8 but was " +
                field.length + "x" +
                (field[0] != null ? field[col].length : 0)
        );
    }
}
