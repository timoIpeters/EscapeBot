package logic;

import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.instructions.Walk;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific tests for the conversion of the Walk instruction.
 *
 * @author Timo Peters
 */
public class GameLogicTest_WalkConversion {

    private final Walk WALK = Walk.getSingleton();

    @Test
    public void testConversionWalkMultipleSteps() {
        //Given
        List<Instruction> programInstructions = List.of(WALK, WALK, WALK);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.WALK, Action.WALK, Action.WALK, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionWalkIntoAWall() {
        //Given
        List<Instruction> programInstructions = List.of(WALK, WALK);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.NORMAL, FieldType.WALL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        // the second walk will not be added to the list of actions, because the player walked against a wall
        List<Action> expected = new ArrayList<>(List.of(Action.WALK, Action.LOOSE_NEXT_FIELD_BLOCKED));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionWalkIntoADoor() {
        //Given
        List<Instruction> programInstructions = List.of(WALK);
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
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_NEXT_FIELD_BLOCKED));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionWalkIntoAGap() {
        //Given
        List<Instruction> programInstructions = List.of(WALK);
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
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_NEXT_FIELD_BLOCKED));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionWalkIntoAGameFieldBorder() {
        //Given
        List<Instruction> programInstructions = List.of(WALK);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.START},
                        {FieldType.NORMAL, FieldType.NORMAL},
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_NEXT_FIELD_BLOCKED));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionCollectACoin() {
        //Given
        List<Instruction> programInstructions = List.of(WALK);
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
        // the second walk will not be added to the list of actions, because the player walked against a wall
        List<Action> expected = new ArrayList<>(List.of(Action.WALK, Action.COLLECT_COIN, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }

}
