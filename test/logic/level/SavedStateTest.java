package logic.level;

import logic.exceptions.InvalidBotRotationException;
import logic.exceptions.InvalidFieldSizeException;
import logic.exceptions.InvalidFieldTypeException;
import logic.exceptions.NoBotRotationException;
import logic.exceptions.NoFieldException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the SavedState class
 *
 * @author Timo Peters
 */
public class SavedStateTest {

    /**
     * validateLoadedState() tests
     */
    @Test(expected = NoBotRotationException.class)
    public void testValidateLoadedState_NoBotRotation() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {4, 3, 3, 3, 3, 3, 3, 2},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5}
        }, null);
        savedState.getLevel();
    }

    @Test(expected = InvalidBotRotationException.class)
    public void testValidateLoadedState_InvalidBotRotation() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {4, 3, 3, 3, 3, 3, 3, 2},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5}
        }, 7);
        savedState.getLevel();
    }

    @Test(expected = NoFieldException.class)
    public void testValidateLoadedState_NoField() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(null, 2);
        savedState.getLevel();

    }

    @Test(expected = InvalidFieldSizeException.class)
    public void testValidateLoadedState_InvalidFieldSize_5x5() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {5, 5, 5, 5, 5},
                {4, 3, 3, 3, 3},
                {5, 5, 5, 5, 2},
                {5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5}
        }, 2);
        savedState.getLevel();
    }

    @Test(expected = InvalidFieldSizeException.class)
    public void testValidateLoadedState_InvalidFieldSize_8x8ButOneCellIsMissing() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {4, 3, 3, 3, 3, 3, 3, 2},
                {5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5}
        }, 2);
        savedState.getLevel();
    }

    @Test(expected = InvalidFieldTypeException.class)
    public void testValidateLoadedState_InvalidFieldType() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {5, 5, 7, 9, 22, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {4, 3, 3, 3, 3, 3, 3, 2},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5}
        }, 2);
        savedState.getLevel();
    }

    @Test
    public void testValidateLoadedState_ValidState() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {4, 3, 3, 3, 3, 3, 3, 2},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5}
        }, 2);
        savedState.getLevel();

    }

    /**
     * getLevel() tests
     */
    @Test
    public void testGetLevel_CorrectLevelRepresentation() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {4, 3, 3, 3, 3, 3, 3, 2},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5}
        }, 1);

        Level res = savedState.getLevel();
        Level expected = new Level(new FieldType[][]{
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
        }, BotRotation.EAST);

        Assert.assertEquals(expected, res);
    }

    @Test
    public void testGetLevel_AnotherCorrectLevelRepresentation() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {4, 5, 3, 3, 3, 5, 5, 5},
                {3, 5, 3, 5, 3, 5, 5, 5},
                {3, 5, 3, 5, 3, 5, 5, 5},
                {3, 5, 3, 5, 3, 5, 5, 5},
                {3, 5, 3, 5, 3, 5, 5, 5},
                {3, 5, 3, 5, 3, 5, 5, 5},
                {3, 5, 3, 5, 3, 5, 5, 5},
                {3, 3, 3, 5, 3, 3, 3, 2}
        }, 2);

        Level res = savedState.getLevel();
        Level expected = new Level(new FieldType[][]{
                {FieldType.START, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.WALL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.SOUTH);

        Assert.assertEquals(expected, res);
    }

    @Test
    public void testGetLevel_ExampleLevel3() throws NoBotRotationException, InvalidBotRotationException, InvalidFieldSizeException, InvalidFieldTypeException, NoFieldException {
        SavedState savedState = new SavedState(new int[][]{
                {4, 3, 3, 3, 3, 3, 3, 1},
                {5, 3, 0, 0, 0, 0, 0, 3},
                {2, 3, 0, 0, 0, 0, 0, 3},
                {3, 0, 0, 0, 0, 0, 0, 3},
                {3, 0, 0, 0, 0, 0, 0, 3},
                {3, 0, 0, 0, 0, 0, 0, 3},
                {3, 0, 0, 0, 0, 0, 0, 3},
                {1, 3, 3, 3, 3, 3, 3, 1}
        }, 1);

        Level res = savedState.getLevel();
        Level expected = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN},
                {FieldType.WALL, FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.DOOR, FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.GAP, FieldType.NORMAL},
                {FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN},

        }, BotRotation.EAST);

        Assert.assertEquals(expected, res);
    }

}