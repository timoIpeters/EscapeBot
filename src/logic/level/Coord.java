package logic.level;


import java.util.Objects;

/**
 * POJO representing a 2-dimensional coordinate (x,y).
 *
 * @author Timo Peters
 */
public class Coord {
    /**
     * Row
     */
    private int row;

    /**
     * Column
     */
    private int col;

    /**
     * Constructor to create a coordinate described by row and column.
     *
     * @param row row index
     * @param col column index
     */
    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Converts a given index to the corresponding grid cell coordinate.
     * For the conversion the grid width is needed (columns).
     *
     * @param idx  index to convert
     * @param cols grid width (amount of columns in the grid)
     * @return converted coordinate
     */
    public static Coord convertIndexToCoord(int idx, int cols) {
        return new Coord(idx / cols, idx % cols);
    }

    /**
     * Converts a given coordinate to the corresponding index of the grid cell.
     * For the conversion teh grid width is needed (columns).
     *
     * @param coord coordinate to convert
     * @param cols  grid width (amount of columns in the grid)
     * @return converted index
     */
    public static int convertCoordToIndex(Coord coord, int cols) {
        return cols * coord.getRow() + coord.getCol();
    }

    /**
     * Returns the next coordinate of a given coordinate in a given direction
     *
     * @param currentPos start coordinate
     * @param rotation   direction to go to
     * @return new coordinate
     */
    public static Coord getNextCoord(Coord currentPos, BotRotation rotation) {
        return new Coord(
                currentPos.getRow() + rotation.getDirectionVector().getRow(),
                currentPos.getCol() + rotation.getDirectionVector().getCol()
        );
    }

    /**
     * Calculates the numeric difference between two coordinates.
     * E.g. the coords (0,0) and (0,1) have a difference of 1 while (0,1) and (0,3) have a difference of 2
     *
     * @param currPos first coordinate
     * @param nextPos second coordinate
     * @return numeric difference of the coordinates
     */
    public static int calculateCoordDifference(Coord currPos, Coord nextPos) {
        return Math.abs(currPos.getRow() - nextPos.getRow()) + Math.abs(currPos.getCol() - nextPos.getCol());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "Coord{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return row == coord.row && col == coord.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
