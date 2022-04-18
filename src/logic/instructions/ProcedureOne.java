package logic.instructions;

import java.util.List;

/**
 * Represents the Procedure 1 instruction.
 *
 * @author Timo Peters
 */
public class ProcedureOne extends Procedure {

    /**
     * Constructor to create an empty procedure one
     */
    public ProcedureOne() {
        super();
    }

    /**
     * Constructor to create a prefilled procedure one from a given list of instructions.
     *
     * @param instructions list of instructions to directly fill procedure one with
     */
    public ProcedureOne(List<Instruction> instructions) {
        super(instructions);
    }

    @Override
    protected InstructionContainer getInstructionContainer() {
        return InstructionContainer.PROCEDURE_ONE;
    }

    @Override
    public String toString() {
        return "AProcedure1";
    }
}
