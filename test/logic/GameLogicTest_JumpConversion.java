package logic;

import logic.conversion.Action;
import logic.instructions.TurnRight;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Instruction;
import logic.instructions.Jump;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Specific tests for the conversion of the Jump instruction.
 *
 * @author Timo Peters
 */
public class GameLogicTest_JumpConversion {

    private final Jump JUMP = Jump.getSingleton();
    private final TurnRight TURN_RIGHT = TurnRight.getSingleton();

    @Test
    public void testConversionJumpOverAGap() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.GAP, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.JUMP_OVER, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionJumpOverANormalField() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_CAN_NOT_JUMP));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionJumpOverADoor() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.DOOR, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_CAN_NOT_JUMP));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionJumpOverACoin() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.COIN, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_CAN_NOT_JUMP));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionCanNotLandOnADoor() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.GAP, FieldType.DOOR},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_CAN_NOT_LAND));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionCanNotLandOnAWall() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.GAP, FieldType.WALL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_CAN_NOT_LAND));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionLandOnACoin() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.GAP, FieldType.COIN},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.JUMP_OVER, Action.COLLECT_COIN, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionJumpOverAGap_TurnAround_JumpBack() {
        //Given
        List<Instruction> programInstructions = List.of(JUMP, TURN_RIGHT, TURN_RIGHT, JUMP);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.GAP, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.JUMP_OVER, Action.TURN_RIGHT, Action.TURN_RIGHT, Action.JUMP_OVER, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }
}
