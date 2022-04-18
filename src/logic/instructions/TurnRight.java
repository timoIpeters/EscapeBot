package logic.instructions;

import logic.conversion.Move;
import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.Level;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents the TurnRight instruction.
 *
 * @author Timo Peters
 */
public class TurnRight implements Instruction {

    /**
     * private TurnRight instance used as a Singleton
     */
    private static final TurnRight SINGLETON = new TurnRight();

    /**
     * private constructor so that there can only be one instance of this class {@link #SINGLETON}.
     */
    private TurnRight() {
    }

    @Override
    public Stream<Move> convertInstruction(Level level, List<InstructionContainer> instructionContainer) {
        // the turn instruction is always possible (as long as it is executed before an exit instruction
        BotRotation preMoveBotRotation = level.getBotRotation();
        level.rotateBotRight();
        return Stream.of(
                new Move(level.getCurrentBotPosition(), preMoveBotRotation, Action.TURN_RIGHT, instructionContainer)
        );
    }

    @Override
    public String toString() {
        return "ARight";
    }

    public static TurnRight getSingleton() {
        return SINGLETON;
    }
}
