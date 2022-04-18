package logic.level;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Coord class
 *
 * @author Timo Peters
 */
public class CoordTest {

    /**
     * convertIndexToCoord() tests
     */
    @Test
    public void testConvertIndexToCoord_IndexZero() {
        Coord res = Coord.convertIndexToCoord(0, 4);
        Assert.assertEquals(new Coord(0, 0), res);
    }


    @Test
    public void testConvertIndexToCoord_IndexOne() {
        Coord res = Coord.convertIndexToCoord(1, 4);
        Assert.assertEquals(new Coord(0, 1), res);
    }

    @Test
    public void testConvertIndexToCoord_IndexOneBelowColSize() {
        Coord res = Coord.convertIndexToCoord(3, 4);
        Assert.assertEquals(new Coord(0, 3), res);
    }

    @Test
    public void testConvertIndexToCoord_IndexEqualToColSize() {
        Coord res = Coord.convertIndexToCoord(4, 4);
        Assert.assertEquals(new Coord(1, 0), res);
    }

    @Test
    public void testConvertIndexToCoord_IndexGreaterThanColSize() {
        Coord res = Coord.convertIndexToCoord(5, 4);
        Assert.assertEquals(new Coord(1, 1), res);
    }

    /**
     * convertCoordToIndex() tests
     */
    @Test
    public void testConvertCoordToIndex_FirstCoordInFirstColumn() {
        Coord coord = new Coord(0, 0);
        int res = Coord.convertCoordToIndex(coord, 4);

        Assert.assertEquals(0, res);
    }

    @Test
    public void testConvertCoordToIndex_LastCoordInFirstColumn() {
        Coord coord = new Coord(0, 3);
        int res = Coord.convertCoordToIndex(coord, 4);

        Assert.assertEquals(3, res);
    }

    @Test
    public void testConvertCoordToIndex_FirstCoordInSecondColumn() {
        Coord coord = new Coord(1, 0);
        int res = Coord.convertCoordToIndex(coord, 4);

        Assert.assertEquals(4, res);
    }

    /**
     * getNextCoord() tests
     */
    @Test
    public void testGetNextCoord_North() {
        Coord currentPos = new Coord(1, 1);
        Coord res = Coord.getNextCoord(currentPos, BotRotation.NORTH);

        Assert.assertEquals(new Coord(0, 1), res);
    }

    @Test
    public void testGetNextCoord_East() {
        Coord currentPos = new Coord(1, 1);
        Coord res = Coord.getNextCoord(currentPos, BotRotation.EAST);

        Assert.assertEquals(new Coord(1, 2), res);
    }

    @Test
    public void testGetNextCoord_South() {
        Coord currentPos = new Coord(1, 1);
        Coord res = Coord.getNextCoord(currentPos, BotRotation.SOUTH);

        Assert.assertEquals(new Coord(2, 1), res);
    }

    @Test
    public void testGetNextCoord_West() {
        Coord currentPos = new Coord(1, 1);
        Coord res = Coord.getNextCoord(currentPos, BotRotation.WEST);

        Assert.assertEquals(new Coord(1, 0), res);
    }
}