package logic;

import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Exit;
import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.instructions.Walk;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific tests for the conversion of the Exit instruction.
 *
 * @author Timo Peters
 */
public class GameLogicTest_ExitConversion {

    private final Exit EXIT = Exit.getSingleton();
    private final Walk WALK = Walk.getSingleton();

    @Test
    public void testConversionExitNextToADoor() {
        //Given
        List<Instruction> programInstructions = List.of(EXIT);
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
        List<Action> expected = new ArrayList<>(List.of(Action.EXIT, Action.WIN));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionExitWhileNotNearADoor() {
        //Given
        List<Instruction> programInstructions = List.of(EXIT);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_DOOR_OUT_OF_REACH));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionExitWhileNotNearADoor_DoorOnTheDiagonal() {
        //Given
        List<Instruction> programInstructions = List.of(EXIT);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.DOOR, FieldType.NORMAL}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_DOOR_OUT_OF_REACH));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionCollectAllCoinsAndGoToExit() {
        //Given
        List<Instruction> programInstructions = List.of(WALK, EXIT);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.COIN, FieldType.DOOR},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.WALK, Action.COLLECT_COIN, Action.EXIT, Action.WIN));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionGoToExitWithoutCollectingAllCoins() {
        //Given
        List<Instruction> programInstructions = List.of(EXIT);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.DOOR, FieldType.COIN},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_UNCOLLECTED_COINS));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversion_WalkAfterExit() {
        //Given
        List<Instruction> programInstructions = List.of(EXIT, WALK);
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.DOOR, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_INSTRUCTIONS_AFTER_EXIT));
        Assert.assertEquals(expected, actions);
    }
}
