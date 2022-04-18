package logic.instructions;

import logic.conversion.Move;
import logic.conversion.Action;
import logic.level.FieldType;
import logic.level.Level;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents the Exit instruction.
 *
 * @author Timo Peters
 */
public class Exit implements Instruction {

    /**
     * private Exit instance used as a Exit-Singleton
     */
    private static final Exit SINGLETON = new Exit();

    /**
     * private constructor so that there can only be one instance of this class {@link #SINGLETON}.
     */
    private Exit() {

    }

    @Override
    public Stream<Move> convertInstruction(Level level, List<InstructionContainer> instructionContainer) {
        // the bot can only exit if he is standing in front of the door
        if (level.getNextCell() == FieldType.DOOR) {
            // the bot can only exit if there are no more coins on the field
            if (level.getCoinAmount() == 0) {
                return Stream.of(
                        new Move(level.getCurrentBotPosition(), level.getBotRotation(), Action.EXIT, instructionContainer)
                );
            }
            return Stream.of(
                    new Move(level.getCurrentBotPosition(), level.getBotRotation(), Action.LOOSE_UNCOLLECTED_COINS, List.of(InstructionContainer.RESULT))
            );
        }
        return Stream.of(
                new Move(level.getCurrentBotPosition(), level.getBotRotation(), Action.LOOSE_DOOR_OUT_OF_REACH, List.of(InstructionContainer.RESULT)));
    }

    @Override
    public String toString() {
        return "AExit";
    }

    public static Exit getSingleton() {
        return SINGLETON;
    }
}
