package logic.solver;

import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Exit;
import logic.instructions.Instruction;
import logic.instructions.Jump;
import logic.instructions.TurnLeft;
import logic.instructions.TurnRight;
import logic.instructions.Walk;
import logic.level.Coord;
import logic.level.Level;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Tests for the LevelSolver class
 *
 * @author Timo Peters
 */
public class LevelSolverTest {
    private final Walk WALK = Walk.getSingleton();
    private final TurnLeft TURN_LEFT = TurnLeft.getSingleton();
    private final TurnRight TURN_RIGHT = TurnRight.getSingleton();
    private final Jump JUMP = Jump.getSingleton();
    private final Exit EXIT = Exit.getSingleton();

    /**
     * solve() tests
     */
    @Test
    public void testSolve() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.NORTH);

        SolverResult res = LevelSolver.solve(level);
    }

    @Test
    public void testSolve_SmallLevel() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.COIN, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.COIN, FieldType.WALL}
        }, BotRotation.EAST);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.SOLVABLE, result.getSolveStatus());
        Assert.assertNotNull(result.getP1());
        Assert.assertNotNull(result.getP2());
        Assert.assertNotNull(result.getProgramInstructions());
    }

    @Test
    public void testSolve_ExampleLevel1() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.SOLVABLE, result.getSolveStatus());
        Assert.assertNotNull(result.getP1());
        Assert.assertNotNull(result.getP2());
        Assert.assertNotNull(result.getProgramInstructions());
        // TODO: what is a good way to test if the instructions the solver made work?
    }

    @Test
    public void testSolve_ExampleLevel2() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL},
                {FieldType.DOOR, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.SOLVABLE, result.getSolveStatus());
        Assert.assertNotNull(result.getP1());
        Assert.assertNotNull(result.getP2());
        Assert.assertNotNull(result.getProgramInstructions());
        // TODO: what is a good way to test if the instructions the solver made work?
    }

    @Test
    public void testSolve_ExampleLevel3() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN},
                {FieldType.WALL, FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.DOOR, FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN}
        }, BotRotation.EAST);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.SOLVABLE, result.getSolveStatus());
        Assert.assertNotNull(result.getP1());
        Assert.assertNotNull(result.getP2());
        Assert.assertNotNull(result.getProgramInstructions());
        // TODO: what is a good way to test if the instructions the solver made work?
    }

    @Test
    public void testSolve_ExampleLevel2_WithExtraGapToLandOnACoin() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.COIN, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.GAP, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL},
                {FieldType.DOOR, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.SOLVABLE, result.getSolveStatus());
        Assert.assertNotNull(result.getP1());
        Assert.assertNotNull(result.getP2());
        Assert.assertNotNull(result.getProgramInstructions());
        // TODO: what is a good way to test if the instructions the solver made work?
    }

    @Test
    public void testSolve_ExampleLevel4() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
        }, BotRotation.SOUTH);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.SOLVABLE, result.getSolveStatus());
        Assert.assertNotNull(result.getP1());
        Assert.assertNotNull(result.getP2());
        Assert.assertNotNull(result.getProgramInstructions());
        // TODO: what is a good way to test if the instructions the solver made work?
    }

    @Test
    public void testSolve_ExampleLevel6() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN, FieldType.COIN},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.GAP, FieldType.GAP, FieldType.WALL, FieldType.WALL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.COIN},
                {FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN, FieldType.COIN},
        }, BotRotation.EAST);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.SOLVABLE, result.getSolveStatus());
        Assert.assertNotNull(result.getP1());
        Assert.assertNotNull(result.getP2());
        Assert.assertNotNull(result.getProgramInstructions());
    }

    @Test
    public void testSolve_ExampleLevel7() {
        // This level can not be solved with the current LevelSolver implementation.
        // To solve this level, the algorithm would have to collect a coin that is further away first. This is not
        // possible with the current implementation, where the LevelSolver always collects the nearest coin first.
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.DOOR, FieldType.WALL, FieldType.COIN, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
        }, BotRotation.EAST);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.UNSOLVABLE_CAN_NOT_REDUCE_TO_PROGRAM_AND_PROCEDURES, result.getSolveStatus());
        Assert.assertNull(result.getP1());
        Assert.assertNull(result.getP2());
        Assert.assertNull(result.getProgramInstructions());
    }

    @Test
    public void testSolve_InvalidLevel() {
        // invalid because there is no START field
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.NORTH);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.UNSOLVABLE_INVALID_LEVEL, result.getSolveStatus());
        Assert.assertNull(result.getP1());
        Assert.assertNull(result.getP2());
        Assert.assertNull(result.getProgramInstructions());
    }

    @Test
    public void testSolve_NoCoinReachable() {
        // invalid because no coin is reachable
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.COIN},
                {FieldType.NORMAL, FieldType.WALL, FieldType.COIN},
                {FieldType.DOOR, FieldType.WALL, FieldType.COIN}
        }, BotRotation.NORTH);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.UNSOLVABLE_CAN_NOT_REACH_ALL_COINS, result.getSolveStatus());
        Assert.assertNull(result.getP1());
        Assert.assertNull(result.getP2());
        Assert.assertNull(result.getProgramInstructions());
    }

    @Test
    public void testSolve_NotAllCoinsReachable() {
        // invalid because one coin is not reachable
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.COIN, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.COIN},
                {FieldType.DOOR, FieldType.WALL, FieldType.WALL}
        }, BotRotation.NORTH);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.UNSOLVABLE_CAN_NOT_REACH_ALL_COINS, result.getSolveStatus());
        Assert.assertNull(result.getP1());
        Assert.assertNull(result.getP2());
        Assert.assertNull(result.getProgramInstructions());
    }

    @Test
    public void testSolve_DoorUnreachable() {
        // invalid because the door is unreachable
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL},
                {FieldType.WALL, FieldType.DOOR}
        }, BotRotation.NORTH);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.UNSOLVABLE_CAN_NOT_REACH_DOOR, result.getSolveStatus());
        Assert.assertNull(result.getP1());
        Assert.assertNull(result.getP2());
        Assert.assertNull(result.getProgramInstructions());
    }

    // TODO look at it
    @Test
    public void testSolve_CanNotConvertToProgramAndInstructions() {
        // invalid because there are to many instruction needed to solve the level
        // so the instruction can not be split into program and procedure instructions
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL},
                {FieldType.COIN, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.START, FieldType.NORMAL, FieldType.GAP, FieldType.WALL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN, FieldType.GAP},
                {FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.WALL, FieldType.WALL, FieldType.GAP, FieldType.WALL, FieldType.COIN},
                {FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.WALL, FieldType.COIN, FieldType.NORMAL, FieldType.COIN, FieldType.DOOR}
        }, BotRotation.NORTH);

        SolverResult result = LevelSolver.solve(level);
        Assert.assertEquals(SolveStatus.UNSOLVABLE_CAN_NOT_REDUCE_TO_PROGRAM_AND_PROCEDURES, result.getSolveStatus());
        Assert.assertNull(result.getP1());
        Assert.assertNull(result.getP2());
        Assert.assertNull(result.getProgramInstructions());
    }

    /**
     * floodFill() tests
     */
    @Test
    public void testFloodFill_FindACoin() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.DOOR},
                {FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.COIN);
        int[][] expected = new int[][]{
                {0, 1, 2, -1},
                {-1, 2, -1, -1},
                {4, 3, 4, -1},
                {-1, -1, -1, -1}
        };
        Assert.assertArrayEquals(expected, floodFillRep);
    }

    @Test
    public void testFloodFill_FindACoin_2() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.DOOR},
                {FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.COIN);
        int[][] expected = new int[][]{
                {0, 1, -1, -1},
                {-1, 2, -1, -1},
                {4, 3, 4, -1},
                {-1, -1, -1, -1}
        };
        Assert.assertArrayEquals(expected, floodFillRep);
    }

    @Test
    public void testFloodFill_FindADoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);
        int[][] expected = new int[][]{
                {0, 1, -1},
                {1, 2, -1},
                {2, 3, 4}
        };
        Assert.assertArrayEquals(expected, floodFillRep);
    }

    @Test
    public void testFloodFill_FindADoorWithGapOnField() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);
        int[][] expected = new int[][]{
                {0, 1, 2, -1},
                {-1, 2, -1, 5},
                {4, 3, 4, 4},
                {-1, -1, -1, -1}
        };
        Assert.assertArrayEquals(expected, floodFillRep);
    }

    @Test
    public void testFloodFill_FindADoorWithGapOnFieldInDifferentBotDirectionThanTheStartingDirection() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.GAP, FieldType.WALL, FieldType.NORMAL},
                {FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);
        int[][] expected = new int[][]{
                {0, 1, 2, -1},
                {-1, 2, -1, 7},
                {-1, 3, -1, 6},
                {-1, 3, 4, 5}
        };
        Assert.assertArrayEquals(expected, floodFillRep);
    }

    @Test
    public void testFloodFill_FullSizeLevelWithMultipleGaps() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);
        int[][] expected = new int[][]{
                {0, 1, -1, 8, 9, 9, 10, 11},
                {1, 2, -1, 7, 8, 8, 9, 10},
                {2, 3, -1, 6, 7, 8, 9, 10},
                {3, 4, 5, 5, 6, 7, 8, 9},
                {4, 5, -1, 6, 7, 8, 8, 9},
                {5, 6, -1, 7, 8, 9, 9, 10},
                {6, 7, -1, 8, 9, 10, 10, 11},
                {7, 7, -1, 9, 10, 11, 11, 12}
        };
        Assert.assertArrayEquals(expected, floodFillRep);
    }

    /**
     * findStartingPosition() tests
     */
    @Test
    public void testFindStartingPosition_TopLeft() {
        int[][] floodFillRep = new int[][]{
                {0, 1, 2, -1},
                {-1, 2, -1, 5},
                {4, 3, 4, 4},
                {-1, -1, -1, -1}
        };

        Assert.assertEquals(new Coord(0, 0), LevelSolver.findStartingPosition(floodFillRep));
    }

    @Test
    public void testFindStartingPosition_SomewhereInTheMiddle() {
        int[][] floodFillRep = new int[][]{
                {2, 1, 2, -1},
                {-1, 0, -1, 3},
                {2, 1, 2, 2},
                {-1, 2, 3, 3}
        };

        Assert.assertEquals(new Coord(1, 1), LevelSolver.findStartingPosition(floodFillRep));
    }

    @Test
    public void testFindStartingPosition_NoStart() {
        // if everything went right then there should never be the case of not finding a starting position
        int[][] floodFillRep = new int[][]{
                {2, 1, 2, -1},
                {-1, 1, -1, 3},
                {2, 1, 2, 2},
                {-1, 2, 3, 3}
        };

        Assert.assertNull(LevelSolver.findStartingPosition(floodFillRep));
    }

    /**
     * createInstructions() tests
     * For this test to work there has to be a working flood fill algorithm!!!
     */
    @Test
    public void testCreateInstructions_ReachTheDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.WALL},
                {FieldType.DOOR, FieldType.WALL, FieldType.WALL}
        }, BotRotation.SOUTH);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);

        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);

        Assert.assertEquals(List.of(WALK, EXIT), result);
    }

    @Test
    public void testCreateInstructions_WalkAndTurnToReachTheDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.SOUTH);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);
        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);

        Assert.assertEquals(List.of(WALK, WALK, TURN_LEFT, WALK, WALK, TURN_LEFT, WALK, EXIT), result);
    }

    @Test
    public void testCreateInstructions_JumpOverAGapAndExit() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.GAP, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);

        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);

        Assert.assertEquals(
                List.of(JUMP, EXIT),
                result
        );
    }

    @Test
    public void testCreateInstructions_DoorUnreachable() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);
        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);

        Assert.assertNull(result);
    }

    @Test
    public void testCreateInstructions_RotateOnceToTheLeftThenGoToTheDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.SOUTH);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);

        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);

        Assert.assertEquals(
                List.of(TURN_LEFT, WALK, EXIT),
                result
        );
    }

    @Test
    public void testCreateInstructions_RotateTwiceThenGoToTheDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.WEST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);

        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);

        Assert.assertEquals(
                List.of(TURN_LEFT, TURN_LEFT, WALK, EXIT),
                result
        );
    }

    @Test
    public void testCreateInstructions_RotateOnceToTheRightThenGoToTheDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.NORTH);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);

        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);

        Assert.assertEquals(
                List.of(TURN_RIGHT, WALK, EXIT),
                result
        );
    }

    @Test
    public void testCreateInstructions_CollectTheCoin() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.WALL},
                {FieldType.GAP, FieldType.DOOR, FieldType.WALL},
                {FieldType.COIN, FieldType.WALL, FieldType.WALL}
        }, BotRotation.SOUTH);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.COIN);
        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, true);

        Assert.assertEquals(List.of(JUMP), result);
    }

    @Test
    public void testCreateInstructions_TurnToTheGapAndJumpOverItOntoTheCoin() {
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.DOOR}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.COIN);
        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, true);

        Assert.assertEquals(List.of(TURN_LEFT, JUMP), result);
    }

    @Test
    public void testCreateInstructions_CoinUnreachable() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.COIN},
                {FieldType.DOOR, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.COIN);
        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, true);

        Assert.assertNull(result);
    }

    @Test
    public void testCreateInstructions_FullSizeLevelWithMultipleGaps() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.GAP, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        int[][] floodFillRep = LevelSolver.floodFill(level, FieldType.DOOR);
        List<Instruction> expected = List.of(
                TURN_RIGHT, WALK, WALK, WALK, TURN_LEFT, WALK, JUMP, TURN_RIGHT, WALK, TURN_LEFT, WALK, JUMP, EXIT
        );
        List<Instruction> result = LevelSolver.createInstructions(level, floodFillRep, false);


        Assert.assertEquals(expected, result);
    }

    /**
     * createInstructionsToReachAllCoins() tests
     * For this tests to work, the createInstructions() tests have to work first!!!
     */
    @Test
    public void testCreateInstructionsToReachAllCoins_NoCoin() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        List<Instruction> result = LevelSolver.createInstructionsToReachAllCoins(level);
        Assert.assertEquals(List.of(), result);
    }

    @Test
    public void testCreateInstructionsToReachAllCoins_BlockedCoins() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.COIN, FieldType.COIN}
        }, BotRotation.EAST);

        List<Instruction> result = LevelSolver.createInstructionsToReachAllCoins(level);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateInstructionsToReachAllCoins_OneCoin() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.COIN, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        List<Instruction> result = LevelSolver.createInstructionsToReachAllCoins(level);
        Assert.assertEquals(List.of(WALK, TURN_RIGHT, WALK), result);
    }

    @Test
    public void testCreateInstructionsToReachAllCoins_MultipleCoins() {
        Level level = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.COIN, FieldType.WALL},
                {FieldType.START, FieldType.COIN, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.COIN, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        List<Instruction> result = LevelSolver.createInstructionsToReachAllCoins(level);
        Assert.assertEquals(List.of(
                WALK, TURN_RIGHT, WALK, TURN_LEFT, TURN_LEFT, WALK, TURN_RIGHT, WALK, TURN_LEFT, WALK
        ), result);
    }

    /**
     * splitInstructionsToProgramAndProcedures() tests
     */
    @Test
    public void testSplitInstructionsToProgramAndProcedures_OnlyProgram() {
        List<Instruction> instructions = List.of(WALK, WALK, WALK, JUMP, WALK, TURN_LEFT);
        SolverResult res = LevelSolver.splitInstructionsToProgramAndProcedures(instructions);

        Assert.assertSame(SolveStatus.SOLVABLE, res.getSolveStatus());
        Assert.assertEquals(instructions, res.getProgramInstructions());
        Assert.assertEquals(new ProcedureOne(), res.getP1());
        Assert.assertEquals(new ProcedureTwo(), res.getP2());
    }

    @Test
    public void testSplitInstructionsToProgramAndProcedures_MaxProgramInstructions() {
        List<Instruction> instructions = List.of(WALK, WALK, WALK, JUMP, WALK, TURN_LEFT,
                TURN_RIGHT, JUMP, WALK, WALK, TURN_LEFT, TURN_LEFT);
        SolverResult res = LevelSolver.splitInstructionsToProgramAndProcedures(instructions);

        Assert.assertSame(SolveStatus.SOLVABLE, res.getSolveStatus());
        Assert.assertEquals(instructions, res.getProgramInstructions());
        Assert.assertEquals(new ProcedureOne(), res.getP1());
        Assert.assertEquals(new ProcedureTwo(), res.getP2());
    }

    @Test
    public void testSplitInstructionsToProgramAndProcedures_ExampleLevel1() {
        List<Instruction> instructions = List.of(
                WALK, WALK, WALK, WALK, WALK, WALK, EXIT
        );
        SolverResult res = LevelSolver.splitInstructionsToProgramAndProcedures(instructions);

        Assert.assertSame(SolveStatus.SOLVABLE, res.getSolveStatus());
        Assert.assertNotNull(res.getProgramInstructions());
        Assert.assertTrue(res.getProgramInstructions().size() <= 12);
        Assert.assertNotNull(res.getP1());
        Assert.assertNotNull(res.getP2());
    }

    @Test
    public void testSplitInstructionsToProgramAndProcedures_ExampleLevel2() {
        List<Instruction> instructions = List.of(
                WALK, WALK, WALK, JUMP, WALK, WALK, TURN_RIGHT, WALK, WALK, TURN_RIGHT,
                WALK, WALK, JUMP, WALK, WALK, EXIT
        );
        SolverResult res = LevelSolver.splitInstructionsToProgramAndProcedures(instructions);

        Assert.assertSame(SolveStatus.SOLVABLE, res.getSolveStatus());
        Assert.assertNotNull(res.getProgramInstructions());
        Assert.assertTrue(res.getProgramInstructions().size() <= 12);
        Assert.assertNotNull(res.getP1());
        Assert.assertNotNull(res.getP2());
    }

    @Test
    public void testSplitInstructionsToProgramAndProcedures_ExampleLevel3() {
        List<Instruction> instructions = List.of(
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, EXIT
        );
        SolverResult res = LevelSolver.splitInstructionsToProgramAndProcedures(instructions);

        Assert.assertSame(SolveStatus.SOLVABLE, res.getSolveStatus());
        Assert.assertNotNull(res.getProgramInstructions());
        Assert.assertTrue(res.getProgramInstructions().size() <= 12);
        Assert.assertNotNull(res.getP1());
        Assert.assertNotNull(res.getP2());
    }

    @Test
    public void testSplitInstructionsToProgramAndProcedures_ExampleLevel4() {
        List<Instruction> instructions = List.of(
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_LEFT, WALK, WALK, TURN_LEFT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT, WALK, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_LEFT, WALK, WALK, EXIT
        );
        SolverResult res = LevelSolver.splitInstructionsToProgramAndProcedures(instructions);

        Assert.assertSame(SolveStatus.SOLVABLE, res.getSolveStatus());
        Assert.assertNotNull(res.getProgramInstructions());
        Assert.assertTrue(res.getProgramInstructions().size() <= 12);
        Assert.assertNotNull(res.getP1());
        Assert.assertNotNull(res.getP2());
    }

    @Test
    public void testSplitInstructionsToProgramAndProcedures_TooManyInstructions() {
        // In this Level the player starts at position (0,0) and has to reach the door at (7,7) while there are coins
        // on any other field. This level is not possible, because there are too many instructions to reduce to program
        // and procedure instructions. It also needs more than two recurring patterns to solve it within the program
        // instruction limits
        List<Instruction> instructions = List.of(
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_LEFT, WALK, TURN_LEFT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_LEFT, WALK, TURN_LEFT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_LEFT, WALK, TURN_LEFT,
                WALK, WALK, WALK, WALK, WALK, WALK, WALK, TURN_RIGHT, WALK, TURN_RIGHT,
                WALK, WALK, WALK, WALK, WALK, WALK, EXIT
        );
        SolverResult res = LevelSolver.splitInstructionsToProgramAndProcedures(instructions);

        Assert.assertSame(SolveStatus.UNSOLVABLE_CAN_NOT_REDUCE_TO_PROGRAM_AND_PROCEDURES, res.getSolveStatus());
        Assert.assertNull(res.getProgramInstructions());
        Assert.assertNull(res.getP1());
        Assert.assertNull(res.getP2());
    }

    /**
     * findOccurrencesOfPermutations() tests
     */
    @Test
    public void testFindOccurrencesOfPermutations_ZeroOrOneInstructions() {
        List<Instruction> instructions1 = List.of();
        List<Instruction> instructions2 = List.of(WALK);

        Map<List<Instruction>, Integer> occurrences1 = LevelSolver.findOccurrencesOfPermutations(instructions1);
        Map<List<Instruction>, Integer> occurrences2 = LevelSolver.findOccurrencesOfPermutations(instructions2);

        Assert.assertTrue(occurrences1.isEmpty());
        Assert.assertTrue(occurrences2.isEmpty());
    }

    @Test
    public void testFindOccurrencesOfPermutations_MultipleInstructions() {
        List<Instruction> instructions = List.of(WALK, TURN_RIGHT, WALK);

        Map<List<Instruction>, Integer> occurrences = LevelSolver.findOccurrencesOfPermutations(instructions);

        Assert.assertFalse(occurrences.isEmpty());
        Assert.assertTrue(occurrences.containsKey(List.of(WALK, TURN_RIGHT)));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(List.of(WALK, TURN_RIGHT)));
        Assert.assertTrue(occurrences.containsKey(List.of(TURN_RIGHT, WALK)));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(List.of(TURN_RIGHT, WALK)));
        Assert.assertTrue(occurrences.containsKey(List.of(WALK, TURN_RIGHT, WALK)));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(List.of(WALK, TURN_RIGHT)));
        Assert.assertEquals(Integer.valueOf(1), occurrences.get(List.of(WALK, TURN_RIGHT, WALK)));
    }

    /**
     * getPermutations() tests
     */
    @Test
    public void testGetPermutations_ZeroOrOneInstructions() {
        List<Instruction> instructions1 = List.of();
        List<Instruction> instructions2 = List.of(WALK);

        List<List<Instruction>> permutations1 = LevelSolver.getPermutations(instructions1);
        List<List<Instruction>> permutations2 = LevelSolver.getPermutations(instructions2);

        Assert.assertTrue(permutations1.isEmpty());
        Assert.assertTrue(permutations2.isEmpty());
    }

    @Test
    public void testGetPermutations_TwoInstructions() {
        List<Instruction> instructions = List.of(
                WALK, TURN_RIGHT
        );
        List<List<Instruction>> permutations = LevelSolver.getPermutations(instructions);

        Assert.assertEquals(1, permutations.size());
        Assert.assertEquals(List.of(WALK, TURN_RIGHT), permutations.get(0));
    }

    @Test
    public void testGetPermutations_ThreeInstructions() {
        List<Instruction> instructions = List.of(
                WALK, TURN_RIGHT, JUMP
        );
        List<List<Instruction>> permutations = LevelSolver.getPermutations(instructions);

        Assert.assertEquals(3, permutations.size());
        Assert.assertTrue(permutations.contains(List.of(WALK, TURN_RIGHT)));
        Assert.assertTrue(permutations.contains(List.of(TURN_RIGHT, JUMP)));
        Assert.assertTrue(permutations.contains(List.of(WALK, TURN_RIGHT, JUMP)));
    }

    @Test
    public void testGetPermutations_FourInstructions() {
        List<Instruction> instructions = List.of(
                WALK, TURN_RIGHT, JUMP, WALK
        );
        List<List<Instruction>> permutations = LevelSolver.getPermutations(instructions);

        Assert.assertEquals(6, permutations.size());
        Assert.assertTrue(permutations.contains(List.of(WALK, TURN_RIGHT)));
        Assert.assertTrue(permutations.contains(List.of(TURN_RIGHT, JUMP)));
        Assert.assertTrue(permutations.contains(List.of(JUMP, WALK)));
        Assert.assertTrue(permutations.contains(List.of(WALK, TURN_RIGHT, JUMP)));
        Assert.assertTrue(permutations.contains(List.of(TURN_RIGHT, JUMP, WALK)));
        Assert.assertTrue(permutations.contains(List.of(WALK, TURN_RIGHT, JUMP, WALK)));
    }

    /**
     * buildSlidingWindows() tests
     */
    @Test
    public void testBuildSlidingWindows_SizeOne() {
        List<Instruction> instructions = List.of(
                WALK, TURN_RIGHT, WALK, JUMP
        );
        List<List<Instruction>> slidingWindows = LevelSolver.buildSlidingWindows(instructions, 1);

        Assert.assertEquals(4, slidingWindows.size());
        Assert.assertTrue(slidingWindows.contains(List.of(WALK)));
        Assert.assertTrue(slidingWindows.contains(List.of(TURN_RIGHT)));
        Assert.assertTrue(slidingWindows.contains(List.of(WALK)));
        Assert.assertTrue(slidingWindows.contains(List.of(JUMP)));
    }

    @Test
    public void testBuildSlidingWindows_SizeTwo() {
        List<Instruction> instructions = List.of(
                WALK, TURN_RIGHT, WALK, JUMP
        );
        List<List<Instruction>> slidingWindows = LevelSolver.buildSlidingWindows(instructions, 2);

        Assert.assertEquals(3, slidingWindows.size());
        Assert.assertTrue(slidingWindows.contains(List.of(WALK, TURN_RIGHT)));
        Assert.assertTrue(slidingWindows.contains(List.of(TURN_RIGHT, WALK)));
        Assert.assertTrue(slidingWindows.contains(List.of(WALK, JUMP)));
    }
}