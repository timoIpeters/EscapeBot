package logic.instructions;

import logic.conversion.Move;
import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.level.Coord;
import logic.level.Level;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents the Walk instruction.
 *
 * @author Timo Peters
 */
public class Walk implements Instruction {

    /**
     * private Walk instance used as a Singleton
     */
    private static final Walk SINGLETON = new Walk();

    /**
     * private constructor so that there can only be one instance of this class {@link #SINGLETON}.
     */
    private Walk() {
    }

    @Override
    public Stream<Move> convertInstruction(Level level, List<InstructionContainer> instructionContainer) {
        Coord preMoveBotPosition = level.getCurrentBotPosition();
        BotRotation preMoveBotRotation = level.getBotRotation();

        FieldType nextCell = level.getNextCell();

        // the bot can only move in the current direction if there is a next cell which is a normal field or a coin
        if (nextCell != null) {
            if (nextCell == FieldType.NORMAL) {
                level.moveToNextCell();
                return Stream.of(
                        new Move(preMoveBotPosition, preMoveBotRotation, Action.WALK, instructionContainer)
                );
            } else if (nextCell == FieldType.COIN) {
                // if the next cell is a coin, it has to be collected
                level.moveToNextCell();
                level.collectCoin();
                return Stream.of(
                        new Move(preMoveBotPosition, preMoveBotRotation, Action.WALK, instructionContainer),
                        new Move(level.getCurrentBotPosition(), level.getBotRotation(), Action.COLLECT_COIN, instructionContainer)
                );
            }
        }
        return Stream.of(
                new Move(preMoveBotPosition, preMoveBotRotation, Action.LOOSE_NEXT_FIELD_BLOCKED, List.of(InstructionContainer.RESULT))
        );
    }

    @Override
    public String toString() {
        return "AWalk";
    }

    public static Walk getSingleton() {
        return SINGLETON;
    }
}
