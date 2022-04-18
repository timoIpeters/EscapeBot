package logic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for the game logic.
 *
 * @author Timo Peters
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GameLogicTest_ExitConversion.class,
        GameLogicTest_JumpConversion.class,
        GameLogicTest_ProcedureConversion.class,
        GameLogicTest_TurnConversion.class,
        GameLogicTest_WalkConversion.class,
        GameLogicTest_GeneralConversion.class,
        GameLogicTest.class
})
public class GameLogicTestSuite {

    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.main(GameLogicTestSuite.class.getName());
    }
}