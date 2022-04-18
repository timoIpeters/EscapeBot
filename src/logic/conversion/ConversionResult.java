package logic.conversion;

import logic.instructions.InstructionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of the conversion from Instructions to Actions.
 * The conversion result contains a list of moves.
 *
 * @author Timo Peters
 */
public class ConversionResult {
    /**
     * List of moves
     */
    private final List<Move> moves;

    /**
     * Constructor to create a conversion result from a set of moves
     *
     * @param moves moves
     */
    public ConversionResult(List<Move> moves) {
        this.moves = moves;
    }

    /**
     * Returns a copy of all moves in the ConversionResult.
     *
     * @return copy of all ConversionResult moves
     */
    public List<Move> getMoves() {
        List<Move> movesCopy = new ArrayList<>();
        for (Move move : moves) {
            List<InstructionContainer> instructionContainersCopy = new ArrayList<>(move.getInstructionContainer());
            Move moveCopy = new Move(move.getCurrentBotPosition(), move.getCurrentBotRotation(), move.getAction(), instructionContainersCopy);
            movesCopy.add(moveCopy);
        }
        return movesCopy;
    }

    /**
     * Returns a copy of the last move in the ConversionResult.
     *
     * @return copy of the last ConversionResult move
     */
    public Move getLastMove() {
        Move lastMove = moves.get(moves.size() - 1);
        List<InstructionContainer> instructionContainersCopy = new ArrayList<>(lastMove.getInstructionContainer());

        return new Move(lastMove.getCurrentBotPosition(), lastMove.getCurrentBotRotation(), lastMove.getAction(), instructionContainersCopy);
    }

    /**
     * Returns the actions of every move in the ConversionResult.
     *
     * @return list of actions from all moves.
     */
    public List<Action> getActions() {
        List<Action> actions = new ArrayList<>();
        moves.forEach(move -> {
            actions.add(move.getAction());
        });
        return actions;
    }

    @Override
    public String toString() {
        return "ConversionResult{" +
                "moves=" + moves +
                '}';
    }
}
