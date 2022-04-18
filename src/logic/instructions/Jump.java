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
 * Represents the Jump instruction.
 *
 * @author Timo Peters
 */
public class Jump implements Instruction {

    /**
     * private Jump instance used as a Singleton
     */
    private static final Jump SINGLETON = new Jump();

    /**
     * private constructor so that there can only be one instance of this class {@link #SINGLETON}.
     */
    private Jump() {

    }

    @Override
    public Stream<Move> convertInstruction(Level level, List<InstructionContainer> instructionContainer) {
        Coord preMoveBotPosition = level.getCurrentBotPosition();
        BotRotation preMoveBotRotation = level.getBotRotation();
        FieldType nextCell = level.getNextCell();

        Stream<Move> converted;
        // the bot can only jump of a gap
        if (nextCell == FieldType.GAP) {
            FieldType landingCell = level.getJumpLandingCell();
            // the bot can land on a normal, start and coin field
            if (landingCell == FieldType.NORMAL || landingCell == FieldType.START) {
                level.jump();
                converted = Stream.of(
                        new Move(preMoveBotPosition, preMoveBotRotation, Action.JUMP_OVER, instructionContainer)
                );
            } else if (landingCell == FieldType.COIN) {
                // when landing on a coin it needs to be collected
                level.jump();
                level.collectCoin();
                converted = Stream.of(
                        new Move(preMoveBotPosition, preMoveBotRotation, Action.JUMP_OVER, instructionContainer),
                        new Move(level.getCurrentBotPosition(), level.getBotRotation(), Action.COLLECT_COIN, instructionContainer)
                );
            } else {
                converted = Stream.of(
                        new Move(preMoveBotPosition, preMoveBotRotation, Action.LOOSE_CAN_NOT_LAND, List.of(InstructionContainer.RESULT))
                );
            }
        } else {
            converted = Stream.of(
                    new Move(preMoveBotPosition, preMoveBotRotation, Action.LOOSE_CAN_NOT_JUMP, List.of(InstructionContainer.RESULT))
            );
        }
        return converted;
    }

    @Override
    public String toString() {
        return "AJump";
    }

    public static Jump getSingleton() {
        return SINGLETON;
    }
}
