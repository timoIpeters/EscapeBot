package logic.conversion;

/**
 * Enum representing all the available actions generated from the players instructions.
 * <p>
 * WIN, LOOSE and RECURSION_ERROR represent the game status flag for the end of the game.
 * If the level was solved successfully the WIN flag will be the last action given by the logic.
 * If the level was not solved successfully the LOOSE flag will be the last action given by the logic.
 * If a recursion was detected while converting instructions into actions the RECURSION_ERROR flag will
 * be the only element in the list of actions.
 *
 * @author Timo Peters
 */
public enum Action {
    /**
     * Action of the Walk instructions
     */
    WALK,
    /**
     * Action of the TurnLeft instruction
     */
    TURN_LEFT,
    /**
     * Action of the TurnRight instruction
     */
    TURN_RIGHT,
    /**
     * Action of the Jump instruction
     */
    JUMP_OVER,
    /**
     * Action used when collecting a coin
     */
    COLLECT_COIN,
    /**
     * Action of the Exit instruction
     */
    EXIT,
    /**
     * Action called when a procedure is empty so that the blue animation frame position can be incremented
     */
    EMPTY_PROCEDURE,
    /**
     * Game result indicating that the player won
     */
    WIN,
    /**
     * Game result indicating that the player lost, because he/she did not place any actions
     */
    LOOSE_NO_ACTIONS,
    /**
     * Game result indicating that the player lost, because the door was not in front of the bot when exiting
     */
    LOOSE_DOOR_OUT_OF_REACH,
    /**
     * Game result indicating that the player lost, because the bot was not able to walk onto the next field
     */
    LOOSE_NEXT_FIELD_BLOCKED,
    /**
     * Game result indicating that the player lost, because the bot can not jump over the next field
     */
    LOOSE_CAN_NOT_JUMP,
    /**
     * Game result indicating that the player lost, because the bot can not land on the next field
     */
    LOOSE_CAN_NOT_LAND,
    /**
     * Game result indicating that the player lost, because there are still coins the bot did not collect
     */
    LOOSE_UNCOLLECTED_COINS,
    /**
     * Game result indicating that the player lost, because the bot did not end the level using the Exit instruction
     */
    LOOSE_NOT_ENDING_WITH_EXIT,
    /**
     * Game result indicating that the player lost, because there were further instructions after calling the exit instruction
     */
    LOOSE_INSTRUCTIONS_AFTER_EXIT,
    /**
     * Game result indicating that the player lost, because either ProcedureOne or ProcedureTwo calls itself
     */
    LOOSE_RECURSION_PROCEDURE_CALLS_ITSELF,
    /**
     * Game result indicating that the player lost, because ProcedureOne and ProcedureTwo call each other
     */
    LOOSE_RECURSION_PROCEDURES_CALL_EACH_OTHER
}
