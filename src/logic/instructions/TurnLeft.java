package logic.instructions;

import logic.conversion.Move;
import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.Level;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents the TurnLeft instruction.
 *
 * @author Timo Peters
 */
public class TurnLeft implements Instruction {

    /**
     * private TurnLeft instance used as a Singleton
     */
    private static final TurnLeft SINGLETON = new TurnLeft();

    /**
     * private constructor so that there can only be one instance of this class {@link #SINGLETON}.
     */
    private TurnLeft() {

    }

    @Override
    public Stream<Move> convertInstruction(Level level, List<InstructionContainer> instructionContainer) {
        // the turn instruction is always possible (as long as it is executed before an exit instruction
        BotRotation preMoveBotRotation = level.getBotRotation();
        level.rotateBotLeft();
        return Stream.of(
                new Move(level.getCurrentBotPosition(), preMoveBotRotation, Action.TURN_LEFT, instructionContainer));
    }

    @Override
    public String toString() {
        return "ALeft";
    }

    public static TurnLeft getSingleton() {
        return SINGLETON;
    }
}
