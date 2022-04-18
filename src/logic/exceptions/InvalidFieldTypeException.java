package logic.exceptions;

import logic.level.Coord;

/**
 * Custom exception which will be thrown if a field type in the loaded level file is invalid.
 *
 * @author Timo Peters
 */
public class InvalidFieldTypeException extends Exception {
    public InvalidFieldTypeException(Coord coord) {
        super("InvalidFieldTypeException: Invalid field type at field (" + coord.getRow() + "," + coord.getCol() + ")");
    }
}
