package logic.instructions;

import java.util.List;

/**
 * Represents the Procedure 2 instruction.
 *
 * @author Timo Peters
 */
public class ProcedureTwo extends Procedure {

    /**
     * Constructor to create an empty procedure two
     */
    public ProcedureTwo() {
        super();
    }

    /**
     * Constructor to create a prefilled procedure two from a given list of instructions.
     *
     * @param instructions list of instructions to directly fill procedure two with
     */
    public ProcedureTwo(List<Instruction> instructions) {
        super(instructions);
    }

    @Override
    protected InstructionContainer getInstructionContainer() {
        return InstructionContainer.PROCEDURE_TWO;
    }

    @Override
    public String toString() {
        return "AProcedure2";
    }
}
