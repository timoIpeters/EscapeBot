package logic.level;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the Level class
 *
 * @author Timo Peters
 */
public class LevelTest {

    /**
     * countCoins() and collectCoin() tests
     */
    @Test
    public void testCountCoins_NoCoins() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST);

        Assert.assertEquals(0, level.getCoinAmount());
    }

    @Test
    public void testCountCoins_OneCoin() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.COIN},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.EAST);

        Assert.assertEquals(1, level.getCoinAmount());
    }

    @Test
    public void testCountCoins_MultipleCoins() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.COIN, FieldType.COIN, FieldType.COIN},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        Assert.assertEquals(5, level.getCoinAmount());
    }

    @Test
    public void testCountCoins_StartWithMultipleCoinsAndCollectSomeOfThem() {
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.COIN, FieldType.COIN, FieldType.COIN},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        Assert.assertEquals(5, level.getCoinAmount());
        level.collectCoin();
        level.collectCoin();
        level.collectCoin();
        Assert.assertEquals(2, level.getCoinAmount());
    }

    /**
     * gameFieldContainsFieldType() tests
     */
    @Test
    public void testGameFieldContainsFieldType_True() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START},
                {FieldType.DOOR, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When, Then
        Assert.assertTrue(level.gameFieldContainsFieldType(FieldType.NORMAL));
    }

    @Test
    public void testGameFieldContainsFieldType_False() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START},
                {FieldType.DOOR, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When, Then
        Assert.assertFalse(level.gameFieldContainsFieldType(FieldType.COIN));
    }

    /**
     * firstOccurrenceOfField() tests
     */
    @Test
    public void testFirstOccurrenceOfField_Included() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START},
                {FieldType.DOOR, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.DOOR);

        Assert.assertEquals(1, occurrence.getRow());
        Assert.assertEquals(0, occurrence.getCol());

        level.setGameFieldCell(FieldType.START, new Coord(0, 0));

        occurrence = level.firstOccurrenceOfField(FieldType.START);

        Assert.assertEquals(0, occurrence.getRow());
        Assert.assertEquals(0, occurrence.getCol());
    }

    @Test
    public void testFirstOccurrenceOfField_NotIncluded() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.DOOR);

        assertNull(occurrence);
    }

    @Test
    public void testFirstOccurrenceOfField_FirstGap() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.COIN, FieldType.GAP, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.GAP);
        Assert.assertEquals(new Coord(2, 1), occurrence);
    }

    @Test
    public void testFirstOccurrenceOfField_FirstCoin() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.COIN, FieldType.GAP, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.COIN);
        Assert.assertEquals(new Coord(2, 0), occurrence);
    }

    @Test
    public void testFirstOccurrenceOfField_FirstDoor() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.COIN, FieldType.GAP, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.DOOR);
        Assert.assertEquals(new Coord(1, 2), occurrence);
    }

    @Test
    public void testFirstOccurrenceOfField_FirstNormalField() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.COIN, FieldType.GAP, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.NORMAL);
        Assert.assertEquals(new Coord(0, 0), occurrence);
    }

    @Test
    public void testFirstOccurrenceOfField_FirstStart() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.COIN, FieldType.GAP, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.START);
        Assert.assertEquals(new Coord(0, 1), occurrence);
    }

    @Test
    public void testFirstOccurrenceOfField_FirstWall() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START, FieldType.WALL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.COIN, FieldType.GAP, FieldType.NORMAL}
        }, BotRotation.EAST);

        //When
        Coord occurrence = level.firstOccurrenceOfField(FieldType.WALL);
        Assert.assertEquals(new Coord(0, 2), occurrence);
    }

    /**
     * allOccurrencesOfField() tests
     */
    @Test
    public void testAllOccurrencesOfField_None() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
        }, BotRotation.NORTH);

        // When
        List<Coord> allOccurrences = level.allOccurrencesOfField(FieldType.START);

        // Then
        Assert.assertNull(allOccurrences);
    }

    @Test
    public void testAllOccurrencesOfField_OneOccurrence() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.START},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
        }, BotRotation.NORTH);

        // When
        List<Coord> allOccurrences = level.allOccurrencesOfField(FieldType.START);

        // Then
        Assert.assertEquals(1, allOccurrences.size());
        Assert.assertEquals(1, allOccurrences.get(0).getRow());
        Assert.assertEquals(2, allOccurrences.get(0).getCol());
    }

    @Test
    public void testAllOccurrencesOfField_MultipleOccurrences() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.START},
                {FieldType.NORMAL, FieldType.START, FieldType.NORMAL},
        }, BotRotation.NORTH);

        // When
        List<Coord> allOccurrences = level.allOccurrencesOfField(FieldType.START);

        // Then
        Assert.assertEquals(4, allOccurrences.size());

        Assert.assertEquals(0, allOccurrences.get(0).getRow());
        Assert.assertEquals(0, allOccurrences.get(0).getCol());

        Assert.assertEquals(1, allOccurrences.get(1).getRow());
        Assert.assertEquals(1, allOccurrences.get(1).getCol());

        Assert.assertEquals(1, allOccurrences.get(2).getRow());
        Assert.assertEquals(2, allOccurrences.get(2).getCol());

        Assert.assertEquals(2, allOccurrences.get(3).getRow());
        Assert.assertEquals(1, allOccurrences.get(3).getCol());
    }

    /**
     * getLevelCopy() test -> to test if we only return a copy of the level
     */
    @Test
    public void testGetLevelCopy() {
        //Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.START},
                {FieldType.DOOR, FieldType.NORMAL}
        }, BotRotation.EAST);
        //When,Then
        assertNotSame(level, level.getLevelCopy());
    }

    /**
     * rotateBotLeft() tests
     */
    @Test
    public void testRotateBotLeft() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        // When
        level.rotateBotLeft();

        // Then
        Assert.assertEquals(BotRotation.NORTH, level.getBotRotation());
    }

    @Test
    public void testRotateBotLeft_FromNorth() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.NORTH);

        // When
        level.rotateBotLeft();

        // Then
        Assert.assertEquals(BotRotation.WEST, level.getBotRotation());
    }

    @Test
    public void testRotateBotLeft_FullRotation() {
        // Given
        BotRotation startDirection = BotRotation.WEST;
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, startDirection);

        // When
        level.rotateBotLeft();
        level.rotateBotLeft();
        level.rotateBotLeft();
        level.rotateBotLeft();

        // Then
        Assert.assertEquals(startDirection, level.getBotRotation());
    }

    /**
     * rotateBotRight() tests
     */
    @Test
    public void testRotateBotRight() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        // When
        level.rotateBotRight();

        // Then
        Assert.assertEquals(BotRotation.SOUTH, level.getBotRotation());
    }

    @Test
    public void testRotateBotRight_FromWest() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.WEST);

        // When
        level.rotateBotRight();

        // Then
        Assert.assertEquals(BotRotation.NORTH, level.getBotRotation());
    }

    @Test
    public void testRotateBotRight_FullRotation() {
        // Given
        BotRotation startDirection = BotRotation.WEST;
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, startDirection);

        // When
        level.rotateBotRight();
        level.rotateBotRight();
        level.rotateBotRight();
        level.rotateBotRight();

        // Then
        Assert.assertEquals(startDirection, level.getBotRotation());
    }

    /**
     * validateLevel() tests
     */
    @Test
    public void testValidateLevel_NoStart() {
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.DOOR}
        }, BotRotation.SOUTH);

        Assert.assertFalse(level.validateLevel());
    }

    @Test
    public void testValidateLevel_NoDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START}
        }, BotRotation.SOUTH);

        Assert.assertFalse(level.validateLevel());
    }

    @Test
    public void testValidateLevel_NoStartAndNoDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.SOUTH);

        Assert.assertFalse(level.validateLevel());
    }

    @Test
    public void testValidateLevel_TooManyStarts() {
        Level level = new Level(new FieldType[][]{
                {FieldType.DOOR, FieldType.START},
                {FieldType.NORMAL, FieldType.START}
        }, BotRotation.SOUTH);

        Assert.assertFalse(level.validateLevel());
    }

    @Test
    public void testValidateLevel_TooManyDoors() {
        Level level = new Level(new FieldType[][]{
                {FieldType.DOOR, FieldType.NORMAL},
                {FieldType.DOOR, FieldType.START}
        }, BotRotation.SOUTH);

        Assert.assertFalse(level.validateLevel());
    }

    @Test
    public void testValidateLevel_ExactlyOneStartAndOneDoor() {
        Level level = new Level(new FieldType[][]{
                {FieldType.DOOR, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START}
        }, BotRotation.SOUTH);

        Assert.assertTrue(level.validateLevel());
    }

    /**
     * getNextCell() tests
     */
    @Test
    public void testGetNextCell_North() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.NORTH);

        // When
        FieldType nextCell = level.getNextCell();

        // Then
        Assert.assertEquals(FieldType.COIN, nextCell);
    }

    @Test
    public void testGetNextCell_East() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.COIN},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        // When
        FieldType nextCell = level.getNextCell();

        // Then
        Assert.assertEquals(FieldType.COIN, nextCell);
    }

    @Test
    public void testGetNextCell_South() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL}
        }, BotRotation.SOUTH);

        // When
        FieldType nextCell = level.getNextCell();

        // Then
        Assert.assertEquals(FieldType.COIN, nextCell);
    }

    @Test
    public void testGetNextCell_West() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.COIN, FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.WEST);

        // When
        FieldType nextCell = level.getNextCell();

        // Then
        Assert.assertEquals(FieldType.COIN, nextCell);
    }

    @Test
    public void testGetNextCell_GameFieldBorder() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.START, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.WEST);

        // When
        FieldType nextCell = level.getNextCell();

        // Then
        assertNull(nextCell);
    }

    /**
     * moveToNextCell() test
     */
    @Test
    public void testMoveToNextCell_North() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.NORTH);

        // When
        level.moveToNextCell();

        // Then
        Assert.assertEquals(FieldType.START, level.getGameFieldCell(0, 1));
        Assert.assertEquals(FieldType.NORMAL, level.getGameFieldCell(1, 1));
    }

    @Test
    public void testMoveToNextCell_East() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.EAST);

        // When
        level.moveToNextCell();

        // Then
        Assert.assertEquals(FieldType.START, level.getGameFieldCell(1, 2));
        Assert.assertEquals(FieldType.NORMAL, level.getGameFieldCell(1, 1));
    }

    @Test
    public void testMoveToNextCell_South() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.SOUTH);

        // When
        level.moveToNextCell();

        // Then
        Assert.assertEquals(FieldType.START, level.getGameFieldCell(2, 1));
        Assert.assertEquals(FieldType.NORMAL, level.getGameFieldCell(1, 1));
    }

    @Test
    public void testMoveToNextCell_West() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.START, FieldType.NORMAL},
                {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL}
        }, BotRotation.WEST);

        // When
        level.moveToNextCell();

        // Then
        Assert.assertEquals(FieldType.START, level.getGameFieldCell(1, 0));
        Assert.assertEquals(FieldType.NORMAL, level.getGameFieldCell(1, 1));
    }

    /**
     * createSavedState() test
     */
    @Test
    public void testCreateSavedState() {
        // Given
        Level level = new Level(new FieldType[][]{
                {FieldType.GAP, FieldType.WALL, FieldType.NORMAL},
                {FieldType.START, FieldType.NORMAL, FieldType.DOOR},
                {FieldType.NORMAL, FieldType.COIN, FieldType.NORMAL}
        }, BotRotation.EAST);

        // When
        SavedState createdSavedState = level.createSavedState();

        // Then
        Assert.assertArrayEquals(new int[][]{
                {0, 5, 3},
                {4, 3, 2},
                {3, 1, 3}
        }, createdSavedState.getGameField());
        Assert.assertEquals(1, createdSavedState.getBotRotation());

    }

}