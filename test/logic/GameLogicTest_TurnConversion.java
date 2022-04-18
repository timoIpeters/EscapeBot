package logic;

import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.instructions.TurnLeft;
import logic.instructions.TurnRight;
import logic.instructions.Walk;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific tests for the conversion of the Turn instructions.
 *
 * @author Timo Peters
 */
public class GameLogicTest_TurnConversion {

    private final TurnLeft TURN_LEFT = TurnLeft.getSingleton();
    private final TurnRight TURN_RIGHT = TurnRight.getSingleton();
    private final Walk WALK = Walk.getSingleton();

    @Test
    public void testConversionTurnLeftAndWalk() {
        //Given
        List<Instruction> programInstructions = List.of(TURN_LEFT, WALK, WALK);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.START, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.TURN_LEFT, Action.WALK, Action.WALK, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionTurnRightAndWalk() {
        //Given
        List<Instruction> programInstructions = List.of(TURN_RIGHT, WALK, WALK);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.START, FieldType.NORMAL}
                },
                BotRotation.WEST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.TURN_RIGHT, Action.WALK, Action.WALK, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }
}
