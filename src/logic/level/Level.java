package logic.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Level POJO used to combine the game field with the bot rotation.
 * This class is also used by the GSON parser to parse from and into JSON.
 *
 * @author Timo Peters
 */
public class Level {
    /**
     * Game field of the level
     */
    private FieldType[][] field;

    /**
     * current bot rotation
     */
    private BotRotation botRotation;

    /**
     * current bot position
     */
    private Coord currentBotPosition;

    /**
     * amount of coins in the level
     */
    private int coins;

    /**
     * Constructor to create a level from a given field and bot rotation.
     *
     * @param field       game field
     * @param botRotation initial bot rotation
     */
    public Level(FieldType[][] field, BotRotation botRotation) {
        this.field = field;
        this.botRotation = botRotation;
        this.currentBotPosition = firstOccurrenceOfField(FieldType.START);
        countCoins();
    }

    /**
     * Counts the amount of coins that exist in the current level
     */
    public void countCoins() {
        List<Coord> coinOccs = allOccurrencesOfField(FieldType.COIN);
        this.coins = coinOccs != null ? coinOccs.size() : 0;
    }

    /**
     * Collect a coin.
     */
    public void collectCoin() {
        this.field[currentBotPosition.getRow()][currentBotPosition.getCol()] = FieldType.NORMAL;
        --this.coins;
    }

    /**
     * Checks if the game field contains a specific field type
     *
     * @param fieldType field type to check
     * @return true if the field type exists in the game field
     */
    public boolean gameFieldContainsFieldType(FieldType fieldType) {
        boolean contains = false;
        int i = 0;
        while (!contains && i < field.length) {
            List<FieldType> row = Arrays.asList(Arrays.asList(field).get(i));
            contains = row.contains(fieldType);
            i++;
        }
        return contains;
    }

    /**
     * Finds the first occurrence of a field type in the game field
     *
     * @param fieldType field type to find the first occurrence of
     * @return coordinate of the first field type occurrence
     */
    public Coord firstOccurrenceOfField(FieldType fieldType) {
        Coord fieldCoord = null;
        int i = 0;
        while (fieldCoord == null && i < field.length) {
            List<FieldType> row = Arrays.asList(Arrays.asList(field).get(i));
            int idx = row.indexOf(fieldType);
            if (idx != -1) {
                fieldCoord = new Coord(i, idx);
            }
            i++;
        }
        return fieldCoord;
    }

    /**
     * finds every occurrence of a field type in the game field.
     *
     * @param fieldType field type to find all occurrences for
     * @return list of coordinates representing the field types locations
     */
    public List<Coord> allOccurrencesOfField(FieldType fieldType) {
        List<Coord> coords = new ArrayList<>();

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == fieldType) {
                    coords.add(new Coord(i, j));
                }
            }
        }

        return coords.size() > 0 ? coords : null;
    }

    /**
     * rotates the bot to the right
     */
    public void rotateBotRight() {
        this.botRotation = this.botRotation.rotateRight();
    }

    /**
     * rotates the bot to the left
     */
    public void rotateBotLeft() {
        this.botRotation = this.botRotation.rotateLeft();
    }

    /**
     * Validates the current level. A level is valid if it contains exactly one start and one door.
     *
     * @return true if the level is valid
     */
    public boolean validateLevel() {
        List<Coord> startFields = allOccurrencesOfField(FieldType.START);
        List<Coord> doors = allOccurrencesOfField(FieldType.DOOR);
        return (startFields != null && startFields.size() == 1) &&
                (doors != null && doors.size() == 1);
    }

    /**
     * Returns the field type of the next cell in the current bot direction.
     *
     * @return next field type
     */
    public FieldType getNextCell() {
        Coord nextCell = Coord.getNextCoord(currentBotPosition, botRotation);
        return getGameFieldCell(nextCell.getRow(), nextCell.getCol());
    }

    public FieldType getJumpLandingCell() {
        Coord nextCell = Coord.getNextCoord(currentBotPosition, botRotation);
        Coord landingPosition = Coord.getNextCoord(nextCell, botRotation);
        return getGameFieldCell(landingPosition.getRow(), landingPosition.getCol());
    }

    /**
     * Moves the player to the next cell in the current bot direction.
     */
    public void moveToNextCell() {
        Coord nextCell = Coord.getNextCoord(currentBotPosition, botRotation);
        moveToCell(nextCell);
    }

    public void jump() {
        Coord nextCell = Coord.getNextCoord(currentBotPosition, botRotation);
        Coord landingPosition = Coord.getNextCoord(nextCell, botRotation);
        moveToCell(landingPosition);
    }

    /**
     * Moves the player to a specified coordinate.
     *
     * @param coord coordinate to move the player to.
     */
    public void moveToCell(Coord coord) {
        this.field[currentBotPosition.getRow()][currentBotPosition.getCol()] = FieldType.NORMAL;
        this.currentBotPosition = new Coord(coord.getRow(), coord.getCol());
        this.field[currentBotPosition.getRow()][currentBotPosition.getCol()] = FieldType.START;
    }

    /**
     * Creates a SavedState from the current level.
     * The saved state is a leaner representation of the current level.
     *
     * @return SavedState representing the current level.
     */
    public SavedState createSavedState() {
        int[][] fields = new int[this.field.length][this.field[0].length];
        int rotation = this.botRotation.ordinal();

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                fields[i][j] = this.field[i][j].ordinal();
            }
        }

        return new SavedState(fields, rotation);
    }

    /**
     * Returns a copy of the current level.
     *
     * @return current level copy
     */
    public Level getLevelCopy() {
        return new Level(
                Arrays.stream(field).map(FieldType[]::clone).toArray(FieldType[][]::new)
                , botRotation);
    }

    /**
     * Returns a copy of the current bot position
     *
     * @return bot position copy
     */
    public Coord getCurrentBotPosition() {
        return new Coord(currentBotPosition.getRow(), currentBotPosition.getCol());
    }

    public FieldType[][] getGameField() {
        return field;
    }

    public FieldType getGameFieldCell(int row, int col) {
        if (row >= 0 && row < this.field.length && col >= 0 && col < this.field[0].length) {
            return field[row][col];
        }
        return null;
    }


    public BotRotation getBotRotation() {
        return botRotation;
    }

    public int getCoinAmount() {
        return this.coins;
    }

    public void setGameField(FieldType[][] gameField) {
        this.field = gameField;
    }

    public void setGameFieldCell(FieldType newFieldType, Coord coord) {
        this.field[coord.getRow()][coord.getCol()] = newFieldType;
        if (newFieldType == FieldType.START) {
            this.currentBotPosition = coord;
        }
    }

    public void setBotRotation(BotRotation botRotation) {
        this.botRotation = botRotation;
    }

    @Override
    public String toString() {
        return "Level{" +
                "field=" + Arrays.toString(field) +
                ", botRotation=" + botRotation +
                ", currentBotPosition=" + currentBotPosition +
                ", coins=" + coins +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return coins == level.coins && Arrays.deepEquals(field, level.field) && botRotation == level.botRotation && Objects.equals(currentBotPosition, level.currentBotPosition);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(botRotation, currentBotPosition, coins);
        result = 31 * result + Arrays.deepHashCode(field);
        return result;
    }
}
