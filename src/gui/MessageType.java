package gui;

/**
 * Enum representing all the available message types for the message dialog.
 *
 * @author Timo Peters
 */
public enum MessageType {
    /**
     * Message type for information.
     * E.g. used when the user won (solved the level) or the LevelSolver was able to find a solution.
     */
    INFORMATION,
    /**
     * Message type for warnings.
     * E.g. used when the level was not solved correctly by the user or the LevelSolver did not find a solution.
     */
    WARNING,
    /**
     * Message type for errors.
     * E.g. used when the level is invalid or when file loading/saving
     * was not possible.
     */
    ERROR
}
