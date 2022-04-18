package logic.conversion;

import logic.instructions.InstructionContainer;
import logic.level.BotRotation;
import logic.level.Coord;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class Representing a Move.
 * A move contains the current bot position, the current bot rotation and the action that should be done.
 *
 * @author Timo Peters
 */
public class Move {
    /**
     * current bot position (before the move happens)
     */
    private final Coord currentBotPosition;

    /**
     * Current bot rotation (before the move happens)
     */
    private final BotRotation currentBotRotation;

    /**
     * Action to do in this move
     */
    private final Action action;

    /**
     * InstructionContainer stack of the move. It contains the InstructionContainers where the current move is called.
     * E.g. when the move is directly called in the program, the instruction stack will be List.of(PROGRAM).
     * E.g. when the move is called in P1, which is called in the program, then the instruction stack will be List.of(PROGRAM, PROCEDURE_ONE)
     */
    private final List<InstructionContainer> instructionContainer;

    /**
     * Constructor to create a move from the currentBotPosition, the currentBotRotation, the action the move represents
     * and the instructionContainer stack the move is called in.
     *
     * @param currentBotPosition   current bot position
     * @param currentBotRotation   current bot direction
     * @param action               action the current move represents
     * @param instructionContainer stack of instruction containers in which the move is called
     */
    public Move(Coord currentBotPosition, BotRotation currentBotRotation, Action action, List<InstructionContainer> instructionContainer) {
        this.currentBotPosition = currentBotPosition;
        this.currentBotRotation = currentBotRotation;
        this.action = action;
        this.instructionContainer = instructionContainer;
    }

    /**
     * Returns a copy of the current bot position
     *
     * @return copy of the current bot position
     */
    public Coord getCurrentBotPosition() {
        return new Coord(currentBotPosition.getRow(), currentBotPosition.getCol());
    }

    public BotRotation getCurrentBotRotation() {
        return currentBotRotation;
    }

    public Action getAction() {
        return action;
    }

    /**
     * Returns the moves instructionContainer stack as an unmodifiable list, so that it is read only.
     *
     * @return instructionContainer stack as unmodifiable list
     */
    public List<InstructionContainer> getInstructionContainer() {
        return Collections.unmodifiableList(instructionContainer);
    }

    @Override
    public String toString() {
        return "Move{" +
                "currentBotPosition=" + currentBotPosition +
                ", currentBotRotation=" + currentBotRotation +
                ", action=" + action +
                ", instructionContainer=" + instructionContainer +
                "}\r\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(currentBotPosition, move.currentBotPosition) && currentBotRotation == move.currentBotRotation && action == move.action && Objects.equals(instructionContainer, move.instructionContainer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentBotPosition, currentBotRotation, action, instructionContainer);
    }
}
