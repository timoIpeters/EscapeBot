package logic.level;

/**
 * Enum representing the direction in which the bot is currently looking
 *
 * @author Timo Peters
 */
public enum BotRotation {
    /**
     * Represents north (0° angle). The direction vector represents the bot going up when facing north.
     */
    NORTH(0, -1, 0),
    /**
     * Represents east (90° angle). The direction vector represents the bot going right when facing east.
     */
    EAST(90, 0, 1),
    /**
     * Represents south (180° angle). The direction vector represents the bot going down when facing south.
     */
    SOUTH(180, 1, 0),
    /**
     * Represents west (270° angle). The direction vector represents the bot going left when facing west.
     */
    WEST(270, 0, -1);

    /**
     * Current bot rotation in degrees
     */
    private final int rotation;

    /**
     * Direction vector indicating in which direction the bot position would change when going in the specified direction.
     */
    private final Coord directionVector;

    /**
     * Constructor to give each BotRotation a rotation angle in degrees and a direction vector.
     *
     * @param rotation rotation angle in degrees
     * @param row      vertical direction, where +1 = down and -1 = up
     * @param col      horizontal direction, wehre +1 = right and -1 = left
     */
    BotRotation(int rotation, int row, int col) {
        this.rotation = rotation;
        this.directionVector = new Coord(row, col);
    }

    /**
     * Calculates the bot rotation needed to get from one coordinate to another. If the two coordinates have a difference
     * in one of the coordinate values of more than one, then they are being capped to one. This restriction is needed
     * to ensure, that the new direction vector is always one of the predefined vectors between -1 and 1.
     * <p>
     * E.g. to be able to go from (1,0) to (2,0), the bot rotation has to be north (direction vector [1,0])
     * <br>
     * E.g. to be able to go from (2,1) to (2,0) the bot rotation has to be east (direction vector [0,-1])
     *
     * @param currPos current position
     * @param nextPos next position to go to
     * @return rotation needed to go from the currPos to the nextPos
     */
    public static BotRotation getRotationChange(Coord currPos, Coord nextPos) {
        // need to adjust the new direction vector if the next coordinate is a gap
        // otherwise the direction vector would contain a +/- 2 which is not a valid direction
        int rowDiff = nextPos.getRow() - currPos.getRow();
        int colDiff = nextPos.getCol() - currPos.getCol();

        int newDirRow = rowDiff > 1 ? 1 : Math.max(rowDiff, -1);
        int newDirCol = colDiff > 1 ? 1 : Math.max(colDiff, -1);

        Coord newDirectionVector = new Coord(newDirRow, newDirCol);
        BotRotation newRotation = null;
        for (BotRotation rotation : BotRotation.values()) {
            if (rotation.getDirectionVector().equals(newDirectionVector)) {
                newRotation = rotation;
            }
        }
        return newRotation;
    }

    /**
     * Rotates the bot to the right by 90 degrees.
     *
     * @return new botRotation
     */
    public BotRotation rotateRight() {
        int newBotRotation = (this.ordinal() + 1) % 4;
        return BotRotation.values()[newBotRotation];
    }

    /**
     * Rotates the bot to the left by 90 degrees.
     *
     * @return new botRotation
     */
    public BotRotation rotateLeft() {
        int decrementedBotRotation = this.ordinal() - 1;
        int newBotRotation = decrementedBotRotation >= 0 ? decrementedBotRotation : BotRotation.values().length - 1;
        return BotRotation.values()[newBotRotation];
    }

    /**
     * Checks if the bot is currently facing a horizontal direction (east and west)
     *
     * @return true if the bot currently faces east or west
     */
    public boolean isHorizontal() {
        return this == BotRotation.EAST || this == BotRotation.WEST;
    }

    public int getRotation() {
        return rotation;
    }

    /**
     * Returns a copy of the current direction vector.
     *
     * @return current direction vector
     */
    public Coord getDirectionVector() {
        return new Coord(directionVector.getRow(), directionVector.getCol());
    }
}
