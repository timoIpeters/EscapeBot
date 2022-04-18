package logic.solver;

/**
 * Indicates if the level is solvable by the LevelSolver.
 * If the level is not solvable, then the SolveStatus has a specific unsolvable status showing why it was not solvable.
 *
 * @author Timo Peters
 */
public enum SolveStatus {
    /**
     * Status indicating that the level is solvable
     */
    SOLVABLE,
    /**
     * Status indicating that the level is invalid and therefore can not be solved
     */
    UNSOLVABLE_INVALID_LEVEL,
    /**
     * Status indicating that the level can not solved, because not all coins are reachable
     */
    UNSOLVABLE_CAN_NOT_REACH_ALL_COINS,
    /**
     * Status indicating that the level can not be solved, because the door is unreachable
     */
    UNSOLVABLE_CAN_NOT_REACH_DOOR,
    /**
     * Status indicating that the level can not be solved, because the instructions can not be split into program and
     * procedure instructions. This can happen, for example, when there were too many instructions.
     */
    UNSOLVABLE_CAN_NOT_REDUCE_TO_PROGRAM_AND_PROCEDURES;
}
