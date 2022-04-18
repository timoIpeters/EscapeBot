package logic.level;

/**
 * Enum representing all the available field types inside a level.
 *
 * @author Timo Peters
 */
public enum FieldType {
    /**
     * Represents a gap, the bot can jump over
     */
    GAP,
    /**
     * Represents a coin, the bot can walk over/on, jump on and collect
     */
    COIN,
    /**
     * Represents a door, the bot can go through
     */
    DOOR,
    /**
     * Represents a normal field, the bot can walk over/on or jump on
     */
    NORMAL,
    /**
     * Representing the field on the game grid where the bot starts. The bot is also able to walk over/on this field or jump on it
     */
    START,
    /**
     * Representing a wall, the bot can not walk or jump over/on
     */
    WALL

}
