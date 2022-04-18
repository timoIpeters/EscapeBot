package logic;

import logic.conversion.Action;
import logic.instructions.Exit;
import logic.instructions.Instruction;
import logic.instructions.Jump;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.instructions.TurnLeft;
import logic.instructions.Walk;
import logic.level.BotRotation;
import logic.level.Coord;
import logic.level.FieldType;
import logic.level.Level;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * General game logic tests that to not include conversion
 *
 * @author Timo Peters
 */
public class GameLogicTest {

    private final Walk WALK = Walk.getSingleton();
    private final Jump JUMP = Jump.getSingleton();
    private final TurnLeft TURN_LEFT = TurnLeft.getSingleton();
    private final Exit EXIT = Exit.getSingleton();

    /**
     * addProgramInstruction() tests
     */
    @Test
    public void testAddProgramInstruction_AddFirstInstruction() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addProgramInstruction(WALK, new Coord(0, 0));

        Assert.assertEquals(1, gameLogic.getProgramInstructions().size());
        Assert.assertEquals(List.of(WALK), gameLogic.getProgramInstructions());
    }

    @Test
    public void testAddProgramInstruction_AddInstructionToAToHighCoordinate() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addProgramInstruction(WALK, new Coord(1, 3));

        Assert.assertEquals(0, gameLogic.getProgramInstructions().size());
    }


    @Test
    public void testAddProgramInstruction_ChangeInstruction() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addProgramInstruction(WALK, new Coord(0, 0));
        gameLogic.addProgramInstruction(WALK, new Coord(0, 1));

        Assert.assertEquals(2, gameLogic.getProgramInstructions().size());
        Assert.assertEquals(List.of(WALK, WALK), gameLogic.getProgramInstructions());

        gameLogic.addProgramInstruction(JUMP, new Coord(0, 1));

        Assert.assertEquals(2, gameLogic.getProgramInstructions().size());
        Assert.assertEquals(List.of(WALK, JUMP), gameLogic.getProgramInstructions());
    }

    @Test
    public void testAddProgramInstruction_ProgramInstructionSizeLimitReached() {
        List<Instruction> programInstructions = new ArrayList<>(List.of(
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK
        ));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, programInstructions, new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(programInstructions, gameLogic.getProgramInstructions());

        gameLogic.addProgramInstruction(JUMP, new Coord(2, 3));

        Assert.assertEquals(programInstructions, gameLogic.getProgramInstructions());
    }

    /**
     * addP1Instruction() tests
     */
    @Test
    public void testAddProcedureOneInstruction_AddFirstInstruction() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addP1Instruction(WALK, new Coord(0, 0));

        Assert.assertEquals(1, gameLogic.getProcedureOneInstructions().size());
        Assert.assertEquals(List.of(WALK), gameLogic.getProcedureOneInstructions());
    }

    @Test
    public void testAddProcedureOneInstruction_AddInstructionToAToHighCoordinate() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addP1Instruction(WALK, new Coord(1, 3));

        Assert.assertEquals(0, gameLogic.getProcedureOneInstructions().size());
    }


    @Test
    public void testAddProcedureOneInstruction_ChangeInstruction() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addP1Instruction(WALK, new Coord(0, 0));
        gameLogic.addP1Instruction(WALK, new Coord(0, 1));

        Assert.assertEquals(2, gameLogic.getProcedureOneInstructions().size());
        Assert.assertEquals(List.of(WALK, WALK), gameLogic.getProcedureOneInstructions());

        gameLogic.addP1Instruction(JUMP, new Coord(0, 1));

        Assert.assertEquals(2, gameLogic.getProcedureOneInstructions().size());
        Assert.assertEquals(List.of(WALK, JUMP), gameLogic.getProcedureOneInstructions());
    }

    @Test
    public void testAddProcedureOneInstruction_ProgramInstructionSizeLimitReached() {
        ProcedureOne p1 = new ProcedureOne();
        p1.addInstructions(List.of(
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK
        ));

        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, List.of(), p1, new ProcedureTwo());

        Assert.assertEquals(p1.getInstructions(), gameLogic.getProcedureOneInstructions());

        gameLogic.addP1Instruction(JUMP, new Coord(2, 3));

        Assert.assertEquals(p1.getInstructions(), gameLogic.getProcedureOneInstructions());
    }

    /**
     * addP2Instruction() test
     */
    @Test
    public void testAddProcedureTwoInstruction_AddFirstInstruction() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addP2Instruction(WALK, new Coord(0, 0));

        Assert.assertEquals(1, gameLogic.getProcedureTwoInstructions().size());
        Assert.assertEquals(List.of(WALK), gameLogic.getProcedureTwoInstructions());
    }

    @Test
    public void testAddProcedureTwoInstruction_AddInstructionToAToHighCoordinate() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addP2Instruction(WALK, new Coord(1, 3));

        Assert.assertEquals(0, gameLogic.getProcedureTwoInstructions().size());
    }


    @Test
    public void testAddProcedureTwoInstruction_ChangeInstruction() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        gameLogic.addP2Instruction(WALK, new Coord(0, 0));
        gameLogic.addP2Instruction(WALK, new Coord(0, 1));

        Assert.assertEquals(2, gameLogic.getProcedureTwoInstructions().size());
        Assert.assertEquals(List.of(WALK, WALK), gameLogic.getProcedureTwoInstructions());

        gameLogic.addP2Instruction(JUMP, new Coord(0, 1));

        Assert.assertEquals(2, gameLogic.getProcedureTwoInstructions().size());
        Assert.assertEquals(List.of(WALK, JUMP), gameLogic.getProcedureTwoInstructions());
    }

    @Test
    public void testAddProcedureTwoInstruction_ProgramInstructionSizeLimitReached() {
        ProcedureTwo p2 = new ProcedureTwo();
        p2.addInstructions(List.of(
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK, WALK
        ));

        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, List.of(), new ProcedureOne(), p2);

        Assert.assertEquals(p2.getInstructions(), gameLogic.getProcedureTwoInstructions());

        gameLogic.addP2Instruction(JUMP, new Coord(2, 3));

        Assert.assertEquals(p2.getInstructions(), gameLogic.getProcedureTwoInstructions());
    }

    /**
     * deleteProgramInstruction() tests
     */
    @Test
    public void testDeleteProgramInstruction_NoInstructions() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(0, gameLogic.getProgramInstructions().size());
        gameLogic.deleteProgramInstruction(new Coord(0, 0));
        Assert.assertEquals(0, gameLogic.getProgramInstructions().size());
    }

    @Test
    public void testDeleteProgramInstruction_Deletable() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(List.of(TURN_LEFT, WALK)), new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProgramInstructions());
        gameLogic.deleteProgramInstruction(new Coord(0, 1));
        Assert.assertEquals(List.of(TURN_LEFT), gameLogic.getProgramInstructions());
    }

    @Test
    public void testDeleteProgramInstruction_NoInstructionAtThatCoord() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(List.of(TURN_LEFT, WALK)), new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProgramInstructions());
        gameLogic.deleteProgramInstruction(new Coord(0, 2));
        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProgramInstructions());
    }

    /**
     * deleteP1Instruction() tests
     */
    @Test
    public void testDeleteP1Instruction_NoInstructions() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(0, gameLogic.getProcedureOneInstructions().size());
        gameLogic.deleteP1Instruction(new Coord(0, 0));
        Assert.assertEquals(0, gameLogic.getProcedureOneInstructions().size());
    }

    @Test
    public void testDeleteP1Instruction_Deletable() {
        ProcedureOne p1 = new ProcedureOne();
        p1.addInstructions(List.of(TURN_LEFT, WALK));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), p1, new ProcedureTwo());

        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProcedureOneInstructions());
        gameLogic.deleteP1Instruction(new Coord(0, 1));
        Assert.assertEquals(List.of(TURN_LEFT), gameLogic.getProcedureOneInstructions());
    }

    @Test
    public void testDeleteP1Instruction_NoInstructionAtThatCoord() {
        ProcedureOne p1 = new ProcedureOne();
        p1.addInstructions(List.of(TURN_LEFT, WALK));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), p1, new ProcedureTwo());

        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProcedureOneInstructions());
        gameLogic.deleteP1Instruction(new Coord(0, 2));
        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProcedureOneInstructions());
    }

    /**
     * deleteP2Instruction() tests
     */
    @Test
    public void testDeleteP2Instruction_NoInstructions() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(0, gameLogic.getProcedureTwoInstructions().size());
        gameLogic.deleteP2Instruction(new Coord(0, 0));
        Assert.assertEquals(0, gameLogic.getProcedureTwoInstructions().size());
    }

    @Test
    public void testDeleteP2Instruction_Deletable() {
        ProcedureTwo p2 = new ProcedureTwo();
        p2.addInstructions(List.of(TURN_LEFT, WALK));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), p2);

        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProcedureTwoInstructions());
        gameLogic.deleteP2Instruction(new Coord(0, 1));
        Assert.assertEquals(List.of(TURN_LEFT), gameLogic.getProcedureTwoInstructions());
    }

    @Test
    public void testDeleteP2Instruction_NoInstructionAtThatCoord() {
        ProcedureTwo p2 = new ProcedureTwo();
        p2.addInstructions(List.of(TURN_LEFT, WALK));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), p2);

        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProcedureTwoInstructions());
        gameLogic.deleteP2Instruction(new Coord(0, 2));
        Assert.assertEquals(List.of(TURN_LEFT, WALK), gameLogic.getProcedureTwoInstructions());
    }

    /**
     * deleteAllProgramInstructions() tests
     */
    @Test
    public void testDeleteAllProgramInstructions_NoInstructions() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        Assert.assertEquals(0, gameLogic.getProgramInstructions().size());
        gameLogic.deleteAllProgramInstructions();
        Assert.assertEquals(0, gameLogic.getProgramInstructions().size());
    }

    @Test
    public void testDeleteAllProgramInstructions_OneInstruction() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(List.of(WALK)), new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(1, gameLogic.getProgramInstructions().size());
        gameLogic.deleteAllProgramInstructions();
        Assert.assertEquals(0, gameLogic.getProgramInstructions().size());
    }

    @Test
    public void testDeleteAllProgramInstructions_MultipleInstructions() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(List.of(WALK, JUMP, WALK, EXIT)), new ProcedureOne(), new ProcedureTwo());

        Assert.assertEquals(4, gameLogic.getProgramInstructions().size());
        gameLogic.deleteAllProgramInstructions();
        Assert.assertEquals(0, gameLogic.getProgramInstructions().size());
    }

    /**
     * deleteAllP1Instructions() tests
     */
    @Test
    public void testDeleteAllP1Instructions_NoInstructions() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        Assert.assertEquals(0, gameLogic.getProcedureOneInstructions().size());
        gameLogic.deleteAllP1Instructions();
        Assert.assertEquals(0, gameLogic.getProcedureOneInstructions().size());
    }

    @Test
    public void testDeleteAllP1Instructions_OneInstruction() {
        ProcedureOne p1 = new ProcedureOne();
        p1.addInstructions(List.of(WALK));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), p1, new ProcedureTwo());

        Assert.assertEquals(1, gameLogic.getProcedureOneInstructions().size());
        gameLogic.deleteAllP1Instructions();
        Assert.assertEquals(0, gameLogic.getProcedureOneInstructions().size());
    }

    @Test
    public void testDeleteAllP1Instructions_MultipleInstructions() {
        ProcedureOne p1 = new ProcedureOne();
        p1.addInstructions(List.of(WALK, JUMP, WALK, EXIT));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), p1, new ProcedureTwo());

        Assert.assertEquals(4, gameLogic.getProcedureOneInstructions().size());
        gameLogic.deleteAllP1Instructions();
        Assert.assertEquals(0, gameLogic.getProcedureOneInstructions().size());
    }

    /**
     * deleteAllP2Instructions() tests
     */
    @Test
    public void testDeleteAllP2Instructions_NoInstructions() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        Assert.assertEquals(0, gameLogic.getProcedureTwoInstructions().size());
        gameLogic.deleteAllP2Instructions();
        Assert.assertEquals(0, gameLogic.getProcedureTwoInstructions().size());
    }

    @Test
    public void testDeleteAllP2Instructions_OneInstruction() {
        ProcedureTwo p2 = new ProcedureTwo();
        p2.addInstructions(List.of(WALK));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), p2);

        Assert.assertEquals(1, gameLogic.getProcedureTwoInstructions().size());
        gameLogic.deleteAllP2Instructions();
        Assert.assertEquals(0, gameLogic.getProcedureTwoInstructions().size());
    }

    @Test
    public void testDeleteAllP2Instructions_MultipleInstructions() {
        ProcedureTwo p2 = new ProcedureTwo();
        p2.addInstructions(List.of(WALK, JUMP, WALK, EXIT));
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), p2);

        Assert.assertEquals(4, gameLogic.getProcedureTwoInstructions().size());
        gameLogic.deleteAllP2Instructions();
        Assert.assertEquals(0, gameLogic.getProcedureTwoInstructions().size());
    }

    /**
     * recursionInProcedures() tests
     */
    @Test
    public void testRecursionInProcedures_NoRecursion() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());
        Assert.assertNull(gameLogic.recursionInProcedures());
    }

    @Test
    public void testRecursionInProcedures_P1CallsItself() {
        ProcedureOne p1 = new ProcedureOne();
        p1.addInstruction(p1);
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), p1, new ProcedureTwo());
        Assert.assertEquals(Action.LOOSE_RECURSION_PROCEDURE_CALLS_ITSELF, gameLogic.recursionInProcedures());
    }

    @Test
    public void testRecursionInProcedures_P2CallsItself() {
        ProcedureTwo p2 = new ProcedureTwo();
        p2.addInstruction(p2);
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), p2);
        Assert.assertEquals(Action.LOOSE_RECURSION_PROCEDURE_CALLS_ITSELF, gameLogic.recursionInProcedures());
    }

    @Test
    public void testRecursionInProcedures_ProceduresCallEachOther() {
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        p1.addInstruction(p2);
        p2.addInstruction(p1);
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), p1, p2);
        Assert.assertEquals(Action.LOOSE_RECURSION_PROCEDURES_CALL_EACH_OTHER, gameLogic.recursionInProcedures());
    }

    /**
     * changeFieldType() tests
     */
    @Test
    public void testChangeFieldType() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        gameLogic.changeFieldType(new Coord(2, 0), FieldType.WALL);
        FieldType[][] expected = new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL}
        };

        Assert.assertArrayEquals(expected, gameLogic.getLevel().getGameField());
    }

    /**
     * resetAllOccurrencesOfField() tests
     */
    @Test
    public void testResetAllOccurrencesOfField_NoOccurrences() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        gameLogic.resetAllOccurrencesOfField(FieldType.WALL);

        FieldType[][] expected = new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        };

        Assert.assertArrayEquals(expected, gameLogic.getLevel().getGameField());
    }

    @Test
    public void testResetAllOccurrencesOfField_OneOccurrence() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        gameLogic.resetAllOccurrencesOfField(FieldType.WALL);

        FieldType[][] expected = new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        };

        Assert.assertArrayEquals(expected, gameLogic.getLevel().getGameField());
    }

    @Test
    public void testResetAllOccurrencesOfField_MultipleOccurrences() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        gameLogic.resetAllOccurrencesOfField(FieldType.WALL);

        FieldType[][] expected = new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        };

        Assert.assertArrayEquals(expected, gameLogic.getLevel().getGameField());
    }

    /**
     * newLevel() test
     */
    @Test
    public void testNewLevel() {
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());
        gameLogic.newLevel();

        FieldType[][] expected = new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,}
        };

        Assert.assertArrayEquals(expected, gameLogic.getLevel().getGameField());
    }

    /**
     * solveLevel() tests -> mostly to test if the GameLogic gets the correct program and procedure instructions.
     * The logic of solving a level will be tested in the LevelSolverTest class.
     */
    @Test
    public void testSolveLevel_Unsolvable() {
        // unsolvable, because there are to many instructions needed
        // would take more than two size 8 procedures to possible split it up to fit in the 12 instructions program size
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL},
                {FieldType.COIN, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.START, FieldType.NORMAL, FieldType.GAP, FieldType.WALL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN, FieldType.GAP},
                {FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.WALL, FieldType.WALL, FieldType.GAP, FieldType.WALL, FieldType.COIN},
                {FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.COIN, FieldType.DOOR}
        }, BotRotation.EAST, new ArrayList<>(), new ProcedureOne(), new ProcedureTwo());

        // initially there are no instructions in program and procedures
        Assert.assertTrue(gameLogic.getProgramInstructions().isEmpty());
        Assert.assertTrue(gameLogic.getProcedureOneInstructions().isEmpty());
        Assert.assertTrue(gameLogic.getProcedureTwoInstructions().isEmpty());

        gameLogic.solveLevel();

        // after the solveLevel() method was called, the program and procedure instructions should still be empty, because
        // the level was not solvable
        Assert.assertTrue(gameLogic.getProgramInstructions().isEmpty());
        Assert.assertTrue(gameLogic.getProcedureOneInstructions().isEmpty());
        Assert.assertTrue(gameLogic.getProcedureTwoInstructions().isEmpty());
    }

    @Test
    public void testSolveLevel_Unsolvable_DontOverrideExistingInstructions() {
        ProcedureOne p1 = new ProcedureOne();
        ProcedureTwo p2 = new ProcedureTwo();
        p1.addInstructions(List.of(WALK, p2));
        p2.addInstructions(List.of(TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT));
        List<Instruction> programInstructions = new ArrayList<>(List.of(WALK, WALK, p1, p1, EXIT));

        // unsolvable, because there are to many instructions needed
        // would take more than two size 8 procedures to possible split it up to fit in the 12 instructions program size
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL},
                {FieldType.COIN, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.START, FieldType.NORMAL, FieldType.GAP, FieldType.WALL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN, FieldType.GAP},
                {FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.WALL, FieldType.WALL, FieldType.GAP, FieldType.WALL, FieldType.COIN},
                {FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.COIN, FieldType.DOOR}
        }, BotRotation.EAST, programInstructions, p1, p2);

        // initially the pre-filled instructions are in program and procedures
        Assert.assertEquals(List.of(WALK, WALK, p1, p1, EXIT), gameLogic.getProgramInstructions());
        Assert.assertEquals(List.of(WALK, p2), gameLogic.getProcedureOneInstructions());
        Assert.assertEquals(List.of(TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT), gameLogic.getProcedureTwoInstructions());

        gameLogic.solveLevel();

        // after the solveLevel() method was called, the program and procedure instructions should still
        // be the same, because the solver found no solution to exchange the old instructions with
        Assert.assertEquals(List.of(WALK, WALK, p1, p1, EXIT), gameLogic.getProgramInstructions());
        Assert.assertEquals(List.of(WALK, p2), gameLogic.getProcedureOneInstructions());
        Assert.assertEquals(List.of(TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT), gameLogic.getProcedureTwoInstructions());
    }

    @Test
    public void testSolveLevel_Solvable() {
        // the level represents example level one, so it is solvable
        GameLogic gameLogic = new GameLogic(new FakeGUI(), new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
        }, BotRotation.EAST);

        // initially there are no instructions in program and procedures
        Assert.assertTrue(gameLogic.getProgramInstructions().isEmpty());
        Assert.assertTrue(gameLogic.getProcedureOneInstructions().isEmpty());
        Assert.assertTrue(gameLogic.getProcedureTwoInstructions().isEmpty());

        gameLogic.solveLevel();

        // after solving the level there should be instructions in program and procedures
        Assert.assertFalse(gameLogic.getProgramInstructions().isEmpty());
        // procedures are still empty, because there are only 7 instructions needed to solve the level
        Assert.assertTrue(gameLogic.getProcedureOneInstructions().isEmpty());
        Assert.assertTrue(gameLogic.getProcedureTwoInstructions().isEmpty());
    }

    /**
     * loadLevelFromFile() tests
     * For those tests to work, the level files must not be inside a folder that contains spaces
     * => caused Java to have problems finding the file
     */
    @Test
    public void testLoadLevelFromFile_ValidFile() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/validFile.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        gameLogic.loadLevelFromFile(file);

        FieldType[][] expectedGameField = new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        };
        BotRotation expectedBotRotation = BotRotation.EAST;

        Assert.assertArrayEquals(expectedGameField, gameLogic.getLevel().getGameField());
        Assert.assertEquals(expectedBotRotation, gameLogic.getLevel().getBotRotation());
    }

    @Test
    public void testLoadLevelFromFile_AnotherValidFile() {
        List<Exception> errorMessages = new ArrayList<>();
        GameLogic gameLogic = new GameLogic(new FakeGUI() {
            @Override
            public void showErrorMessage(Exception e) {
                errorMessages.add(e);
            }
        });
        URL url = this.getClass().getResource("/logic/testfiles/anotherValidFile.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        gameLogic.loadLevelFromFile(file);

        FieldType[][] expectedGameField = new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR}
        };
        BotRotation expectedBotRotation = BotRotation.SOUTH;

        Assert.assertArrayEquals(expectedGameField, gameLogic.getLevel().getGameField());
        Assert.assertEquals(expectedBotRotation, gameLogic.getLevel().getBotRotation());
        Assert.assertEquals(List.of(), errorMessages);
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_Empty() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/empty.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
        gameLogic.loadLevelFromFile(file);
        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_InvalidBotRotation() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/invalidBotRotation.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
        gameLogic.loadLevelFromFile(file);
        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_NoBotRotation() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/noBotRotation.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
        gameLogic.loadLevelFromFile(file);
        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_BotRotationNull() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/noBotRotation.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
        gameLogic.loadLevelFromFile(file);
        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_InvalidFieldSize_5x5() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/invalidFieldSize.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());

        gameLogic.loadLevelFromFile(file);

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_InvalidFieldSize_OneCellMissing() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/invalidFieldSize_OneCellMissing.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());

        gameLogic.loadLevelFromFile(file);

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_NoField() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/noField.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());

        gameLogic.loadLevelFromFile(file);

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_FieldNull() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/fieldNull.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());

        gameLogic.loadLevelFromFile(file);

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_InvalidFieldType() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/invalidFieldType.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());

        gameLogic.loadLevelFromFile(file);

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    @Test
    public void testLoadLevelFromFile_InvalidFile_InvalidJson() {
        GameLogic gameLogic = new GameLogic(new FakeGUI());
        URL url = this.getClass().getResource("/logic/testfiles/invalidJson.json");
        File file = new File(Objects.requireNonNull(url).getFile());

        Level levelBeforeLoading = gameLogic.getLevel();

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());

        gameLogic.loadLevelFromFile(file);

        Assert.assertEquals(levelBeforeLoading, gameLogic.getLevel());
    }

    /**
     * initializeGameField() tests
     */
    @Test
    public void testInitializeGameField() {
        Level initialLevel = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.NORTH);
        Level newLevel = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.SOUTH);
        GameLogic gameLogic = new GameLogic(new FakeGUI(), initialLevel.getGameField(), initialLevel.getBotRotation());

        Assert.assertEquals(initialLevel, gameLogic.getLevel());

        gameLogic.initializeGameField(newLevel);

        Assert.assertEquals(newLevel, gameLogic.getLevel());
    }

}