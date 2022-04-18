package logic.instructions;

import logic.conversion.Move;
import logic.level.Level;

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface for the available game instructions.
 *
 * @author Timo Peters
 */
public interface Instruction {
    /**
     * Converts an instruction to a stream of actions.
     *
     * @param level                current level instance to validate the instruction
     * @param instructionContainer stack of instructionContainers, the current instruction is called in
     * @return stream of actions created from the instruction
     */
    Stream<Move> convertInstruction(Level level, List<InstructionContainer> instructionContainer);
}
