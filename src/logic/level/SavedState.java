package logic.level;

import logic.exceptions.InvalidBotRotationException;
import logic.exceptions.InvalidFieldSizeException;
import logic.exceptions.InvalidFieldTypeException;
import logic.exceptions.NoBotRotationException;
import logic.exceptions.NoFieldException;

import java.util.Arrays;

/**
 * This POJO acts as the minimized level representation wich is used to save/load levels.
 *
 * @author Timo Peters
 */
public class SavedState {
    /**
     * game fields represented as integer values
     */
    private final int[][] field;

    /**
     * bot rotation represented as integer value
     */
    private final Integer botRotation;

    /**
     * Constructor to create a SavedState from a given game field and bot rotation.
     *
     * @param field       game field
     * @param botRotation initial bot rotation
     */
    public SavedState(int[][] field, Integer botRotation) {
        this.field = field;
        this.botRotation = botRotation;
    }

    /**
     * Creates a level out of the SavedState.
     *
     * @return Level representing this saved state
     * @throws InvalidBotRotationException when it is not within the bounds of 0 to 3
     * @throws InvalidFieldTypeException   when at least one field is not within the bounds of 0 to 5
     * @throws InvalidFieldSizeException   when the game field is not 8x8
     * @throws NoFieldException            when there is no "field" property, or it is null
     * @throws NoBotRotationException      when there is no "botRotation" property, or it is null
     */
    public Level getLevel() throws InvalidBotRotationException, InvalidFieldTypeException, InvalidFieldSizeException, NoFieldException, NoBotRotationException {
        validateLoadedState();
        FieldType[][] fields = new FieldType[this.field.length][this.field[0].length];
        BotRotation rotation = BotRotation.values()[this.botRotation];

        // go through every field of the loaded state and set the game fields field type at that position
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                fields[i][j] = FieldType.values()[this.field[i][j]];
            }
        }

        return new Level(fields, rotation);
    }


    /**
     * Validates the loaded state by validation the bot rotation and all the field types
     *
     * @throws InvalidBotRotationException when it is not within the bounds of 0 to 3
     * @throws InvalidFieldTypeException   when at least one field is not within the bounds of 0 to 5
     * @throws InvalidFieldSizeException   when the game field is not 8x8
     * @throws NoFieldException            when there is no "field" property, or it is null
     * @throws NoBotRotationException      when there is no "botRotation" property, or it is null
     */
    private void validateLoadedState() throws InvalidBotRotationException, InvalidFieldTypeException, InvalidFieldSizeException, NoFieldException, NoBotRotationException {
        if (botRotation == null) {
            throw new NoBotRotationException();
        }

        if (botRotation < 0 || botRotation > 3) {
            throw new InvalidBotRotationException(botRotation);
        }

        if (field == null) {
            throw new NoFieldException();
        }

        if (field.length != 8 || field[0] == null) {
            throw new InvalidFieldSizeException(field, 0);
        }

        // check every row if it has the correct length
        int i = 0;
        boolean invalidCols = false;
        while (!invalidCols && i < field.length) {
            invalidCols = field[i].length != 8;
            i++;
        }

        if (invalidCols) {
            throw new InvalidFieldSizeException(field, i - 1);
        }

        // validate field types
        Coord invalidFieldType = null;

        // go through every field and check if the field type is within the bounds
        for (int row = 0; row < field.length && invalidFieldType == null; row++) {
            for (int col = 0; col < field[row].length && invalidFieldType == null; col++) {
                int fieldType = field[row][col];
                if (fieldType < 0 || fieldType > 5) {
                    invalidFieldType = new Coord(row, col);
                }
            }
        }

        if (invalidFieldType != null) {
            throw new InvalidFieldTypeException(invalidFieldType);
        }
    }

    protected int getBotRotation() {
        return botRotation;
    }

    /**
     * Returns a copy of the current game field.
     *
     * @return game field copy
     */
    protected int[][] getGameField() {
        return Arrays.stream(field).map(int[]::clone).toArray(int[][]::new);
    }
}
