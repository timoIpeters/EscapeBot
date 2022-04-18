package logic.conversion;

import logic.instructions.InstructionContainer;
import logic.level.BotRotation;
import logic.level.Coord;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ConversionResultTest {

    /**
     * getMoves() test
     */
    @Test
    public void testGetMoves() {
        List<Move> listOfMoves = List.of(
                new Move(new Coord(0, 0), BotRotation.EAST, Action.WALK, List.of(InstructionContainer.PROGRAM)),
                new Move(new Coord(0, 1), BotRotation.EAST, Action.WALK, List.of(InstructionContainer.PROGRAM)),
                new Move(new Coord(0, 2), BotRotation.EAST, Action.EXIT, List.of(InstructionContainer.PROGRAM)),
                new Move(new Coord(0, 2), BotRotation.EAST, Action.WIN, List.of(InstructionContainer.PROGRAM))
        );
        ConversionResult conversionResult = new ConversionResult(listOfMoves);
        List<Move> res = conversionResult.getMoves();

        Assert.assertEquals(listOfMoves, res);
    }

    /**
     * getLastMove() test
     */
    @Test
    public void testGetLastMove() {
        List<Move> listOfMoves = List.of(
                new Move(new Coord(0, 0), BotRotation.EAST, Action.WALK, List.of(InstructionContainer.PROGRAM)),
                new Move(new Coord(0, 1), BotRotation.EAST, Action.WALK, List.of(InstructionContainer.PROGRAM)),
                new Move(new Coord(0, 2), BotRotation.EAST, Action.EXIT, List.of(InstructionContainer.PROGRAM)),
                new Move(new Coord(0, 2), BotRotation.EAST, Action.WIN, List.of(InstructionContainer.PROGRAM))
        );
        ConversionResult conversionResult = new ConversionResult(listOfMoves);
        Move res = conversionResult.getLastMove();

        Assert.assertEquals(
                new Move(new Coord(0, 2), BotRotation.EAST, Action.WIN, List.of(InstructionContainer.PROGRAM))
                , res);
    }

}