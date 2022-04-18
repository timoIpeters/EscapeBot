package logic.instructions;

/**
 * Enum representing an instruction container wehre an instruction is called in. It is used in a instructionContainer
 * stack which shows where an instruction is currently called in.
 *
 * @author Timo Peters
 */
public enum InstructionContainer {
    /**
     * Program instruction container
     */
    PROGRAM,
    /**
     * ProcedureOne instruction container
     */
    PROCEDURE_ONE,
    /**
     * ProcedureTwo instruction container
     */
    PROCEDURE_TWO,
    /**
     * Win or loose state (last action of the level)
     */
    RESULT
}
