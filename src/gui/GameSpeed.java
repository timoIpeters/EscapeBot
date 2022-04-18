package gui;

/**
 * Enum representing the available animation speeds of the game in milliseconds.
 *
 * @author Timo Peters
 */
public enum GameSpeed {
    /**
     * No animation speed (0 millis per instruction)
     */
    NONE(0),
    /**
     * Slow animation speed (2 seconds per instructions)
     */
    SLOW(1500),
    /**
     * Normal animation speed (1.5 seconds per instruction)
     */
    NORMAL(1000),
    /**
     * Fast animation speed (1 second per instruction)
     */
    FAST(750),
    /**
     * Faster animation speed (0.5 seconds per instruction)
     */
    FASTER(500);

    /**
     * Game speed in millis.
     */
    private final double speed;

    /**
     * Constructor to give each GameSpeed a speed value in millis.
     *
     * @param speed animation speed in millis
     */
    GameSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
