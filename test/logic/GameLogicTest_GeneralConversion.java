package logic;

import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Exit;
import logic.instructions.Instruction;
import logic.instructions.Jump;
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
 * General tests for the conversion of different instructions.
 *
 * @author Timo Peters
 */
public class GameLogicTest_GeneralConversion {

    private final Exit EXIT = Exit.getSingleton();
    private final Jump JUMP = Jump.getSingleton();
    private final TurnLeft TURN_LEFT = TurnLeft.getSingleton();
    private final TurnRight TURN_RIGHT = TurnRight.getSingleton();
    private final Walk WALK = Walk.getSingleton();

    @Test
    public void testAddMaxAmountOfProgramInstructions() {
        //Given
        List<Instruction> programInstructions = List.of(
                TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT,
                TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT
        );
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
        List<Action> expected = new ArrayList<>(List.of(
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.LOOSE_NOT_ENDING_WITH_EXIT
        ));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testAddTooManyProgramInstructions() {
        //Given
        List<Instruction> programInstructions = List.of(
                TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT,
                TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT,
                TURN_LEFT, TURN_LEFT, TURN_LEFT
        );
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
        List<Action> expected = List.of(
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.LOOSE_NOT_ENDING_WITH_EXIT
        );
        Assert.assertEquals(expected.size(), actions.size());
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testAddMaxAmountOfProcedureOneInstructions() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        List<Instruction> programInstructions = List.of(p1);

        for (int i = 0; i < 8; i++) {
            p1.addInstruction(TURN_LEFT);
        }

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
        List<Action> expected = new ArrayList<>(List.of(
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.TURN_LEFT, Action.TURN_LEFT, Action.LOOSE_NOT_ENDING_WITH_EXIT
        ));
        Assert.assertEquals(8, p1.getInstructions().size());
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testAddTooManyProcedureOneInstructions() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        List<Instruction> programInstructions = List.of(p1);

        for (int i = 0; i < 9; i++) {
            p1.addInstruction(TURN_LEFT);
        }

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
        List<Action> expected = new ArrayList<>(List.of(
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.TURN_LEFT, Action.TURN_LEFT, Action.LOOSE_NOT_ENDING_WITH_EXIT
        ));
        Assert.assertEquals(8, p1.getInstructions().size());
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testAddMaxAmountOfProcedureTwoInstructions() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        List<Instruction> programInstructions = List.of(p2);

        for (int i = 0; i < 8; i++) {
            p2.addInstruction(TURN_LEFT);
        }

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
        List<Action> expected = new ArrayList<>(List.of(
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.TURN_LEFT, Action.TURN_LEFT, Action.LOOSE_NOT_ENDING_WITH_EXIT
        ));
        Assert.assertEquals(8, p2.getInstructions().size());
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testAddTooManyProcedureTwoInstructions() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        List<Instruction> programInstructions = List.of(p2);

        for (int i = 0; i < 9; i++) {
            p2.addInstruction(TURN_LEFT);
        }

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
        List<Action> expected = new ArrayList<>(List.of(
                Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT, Action.TURN_LEFT,
                Action.TURN_LEFT, Action.TURN_LEFT, Action.LOOSE_NOT_ENDING_WITH_EXIT
        ));
        Assert.assertEquals(8, p2.getInstructions().size());
        Assert.assertEquals(expected, actions);
    }
}
