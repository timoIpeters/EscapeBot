package logic;

import logic.conversion.Action;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.instructions.TurnRight;
import logic.instructions.Walk;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific tests for the conversion of the Procedure instructions.
 *
 * @author Timo Peters
 */
public class GameLogicTest_ProcedureConversion {

    private final Walk WALK = Walk.getSingleton();
    private final TurnRight TURN_RIGHT = TurnRight.getSingleton();

    /* Conversion between Instructions and Actions (ProgrammInstructions and P1-/P2Instructions) */
    @Test
    public void testConversionProgramCallsP1Once() {
        ProcedureOne p1 = new ProcedureOne(List.of(WALK, WALK));
        ProcedureTwo p2 = new ProcedureTwo();
        List<Instruction> programInstructions = List.of(p1);

        //Given
        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2
        );

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.WALK, Action.WALK, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void someTest() {
        ProcedureOne p1 = new ProcedureOne();
        boolean added = p1.addInstructions(List.of(WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK));
        Assert.assertTrue(added);

        ProcedureOne anotherP1 = new ProcedureOne();
        boolean added2 = anotherP1.addInstructions(List.of(WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK));
        Assert.assertFalse(added2);

    }

    @Test
    public void someTest_v2() {
        ProcedureOne p1 = new ProcedureOne();
        boolean[] added = {
                p1.addInstruction(WALK),
                p1.addInstruction(WALK),
                p1.addInstruction(WALK),
                p1.addInstruction(WALK),
                p1.addInstruction(WALK),
                p1.addInstruction(WALK),
                p1.addInstruction(WALK),
                p1.addInstruction(WALK)
        };

        for (boolean insAdded : added) {
            Assert.assertTrue(insAdded);
        }

        boolean anotherAdded = p1.addInstruction(WALK);
        Assert.assertFalse(anotherAdded);
    }

    @Test
    public void testConversionProgramCallsP1MultipleTimes() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        p1.addInstruction(WALK);
        p1.addInstruction(TURN_RIGHT);
        List<Instruction> programInstructions = List.of(p1, p1, p1, p1);

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(
                List.of(
                        Action.WALK, Action.TURN_RIGHT, Action.WALK, Action.TURN_RIGHT,
                        Action.WALK, Action.TURN_RIGHT, Action.WALK, Action.TURN_RIGHT,
                        Action.LOOSE_NOT_ENDING_WITH_EXIT)
        );
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testConversionProgramCallsP1WhichCallsP2() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        p1.addInstruction(p2);
        p2.addInstruction(WALK);
        p2.addInstruction(WALK);
        List<Instruction> programInstructions = List.of(p1);

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
        List<Action> expected = new ArrayList<>(List.of(Action.WALK, Action.WALK, Action.LOOSE_NOT_ENDING_WITH_EXIT));
        Assert.assertEquals(expected, actions);
    }

    /* Validity tests for the Instructions */
    @Test
    public void testIllegalRecursionP1CallsItself() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        p1.addInstruction(p1);
        List<Instruction> programInstructions = List.of(p1);

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.START, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.DOOR}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_RECURSION_PROCEDURE_CALLS_ITSELF));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testIllegalRecursionP2CallsItself() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        p2.addInstruction(p2);
        List<Instruction> programInstructions = List.of(p2);

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.START, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.DOOR}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_RECURSION_PROCEDURE_CALLS_ITSELF));
        Assert.assertEquals(expected, actions);
    }

    @Test
    public void testIllegalRecursionProceduresCallEachOther() {
        //Given
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        p1.addInstruction(p2);
        p2.addInstruction(p1);
        List<Instruction> programInstructions = List.of(p1);

        GameLogic gameLogic = new GameLogic(
                new FakeGUI(),
                new FieldType[][]{
                        {FieldType.START, FieldType.NORMAL},
                        {FieldType.NORMAL, FieldType.DOOR}
                },
                BotRotation.EAST,
                programInstructions,
                p1,
                p2);

        //When
        List<Action> actions = gameLogic.convertInstructionsToActions().getActions();

        //Then
        List<Action> expected = new ArrayList<>(List.of(Action.LOOSE_RECURSION_PROCEDURES_CALL_EACH_OTHER));
        Assert.assertEquals(expected, actions);
    }
}
