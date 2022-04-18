package logic.solver;

import logic.instructions.Exit;
import logic.instructions.Jump;
import logic.instructions.Procedure;
import logic.instructions.TurnLeft;
import logic.instructions.TurnRight;
import logic.instructions.Walk;
import logic.level.BotRotation;
import logic.level.FieldType;
import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.level.Coord;
import logic.level.Level;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * Level Solver using a variant of flood-fill.
 * The algorithm will try to find a solution for a given level and generate a set of instructions to solve the level.
 *
 * @author Timo Peters
 */
public class LevelSolver {

    /**
     * Constant describing the maximum amount of program instructions
     */
    private static final int MAX_PROGRAM_INSTRUCTIONS = 12;

    /**
     * Constant describing the maximum amount of procedure instructions
     */
    private static final int MAX_PROCEDURE_INSTRUCTIONS = 8;

    /**
     * Finds a solution for the given level. The algorithm uses FloodFill to find a path from the start field, over every
     * coin on the field, to the door. When finding a path the path will be translated into instructions which are then
     * reduced into program and procedure instructions.
     * <p>
     * If there is no path to the door with all coins collected or the generated instructions are not within the bounds
     * of the maximum possible program and procedure instructions, the SolverStatus of the SolverResult will be one of
     * the available UNSOLVABLE status.
     *
     * @param level level to solve
     * @return SolverResult containing the program and procedure instructions with a SOLVED status or null for the instructions and a UNSOLVABLE status if the level is not solvable.
     */
    public static SolverResult solve(Level level) {
        if (!level.validateLevel()) {
            return new SolverResult(null, null, null, SolveStatus.UNSOLVABLE_INVALID_LEVEL);
        }

        // using a copy of the level, because the algorithm changes the level.currentBotPosition and the amount of coins
        Level levelCopy = level.getLevelCopy();

        // create instructions to all coins
        List<Instruction> instructions = createInstructionsToReachAllCoins(levelCopy);

        if (instructions == null) {
            return new SolverResult(null, null, null, SolveStatus.UNSOLVABLE_CAN_NOT_REACH_ALL_COINS);
        }

        // create instructions to the door
        int[][] pathToDoorRep = floodFill(levelCopy, FieldType.DOOR);
        List<Instruction> instructionsToDoor = createInstructions(levelCopy, pathToDoorRep, false);

        if (instructionsToDoor == null) {
            return new SolverResult(null, null, null, SolveStatus.UNSOLVABLE_CAN_NOT_REACH_DOOR);
        }

        instructions.addAll(instructionsToDoor);

        return splitInstructionsToProgramAndProcedures(instructions);
    }

    /**
     * Uses the {@link #floodFill(Level, FieldType)} and the {@link #createInstructions(Level, int[][], boolean)} methods to
     * create a list of instructions from the bots starting position to the last coin, while collecting all other coins
     * on the way.
     *
     * @param level level in which the coins should be collected
     * @return list of instructions that are needed to reach every coin from the bots starting position, or null if
     * there was at least one unreachable coin
     */
    static List<Instruction> createInstructionsToReachAllCoins(Level level) {
        int coinsInLevel = level.getCoinAmount();
        List<Instruction> instructions = new ArrayList<>();
        boolean coinsReachable = true;

        // try to create the instructions needed to reach all coins
        // after every iteration the starting position will be set to the last found coin
        while (coinsReachable && coinsInLevel > 0) {
            int[][] pathToCoin = floodFill(level, FieldType.COIN);
            List<Instruction> instructionsToCoin = createInstructions(level, pathToCoin, true);

            if (instructionsToCoin == null) {
                coinsReachable = false;
            } else {
                instructions.addAll(instructionsToCoin);
                coinsInLevel--;
            }
        }

        // null indicates that there were unreachable coins
        return coinsReachable ? instructions : null;
    }

    /**
     * Applies the FloodFill-algorithm to the given level. The result will be an array of steps from the start, over every
     * coin, to the door.
     * <p>
     * This method is package-private for test-purposes
     *
     * @param level level to apply FloodFill on
     * @return array of steps from the start, over every coin, to the door
     */
    static int[][] floodFill(Level level, FieldType stopAt) {
        // initialize level
        int gameWidth = level.getGameField().length;
        int[][] levelRep = new int[gameWidth][gameWidth];
        for (int[] ints : levelRep) {
            Arrays.fill(ints, -1);
        }
        levelRep[level.getCurrentBotPosition().getRow()][level.getCurrentBotPosition().getCol()] = 0;

        // initialize the flood fill stack with the starting field
        // the stack represents the cell positions we still have to check
        Queue<Coord> cells = new LinkedList<>();
        cells.add(level.getCurrentBotPosition());

        boolean endFound = false;
        Coord lastCellPos = null;

        // go through the stack of cell coordinates until stopAt was found or there is no new field to check
        while (!endFound && !cells.isEmpty()) {
            Coord lastCell = cells.remove();
            lastCellPos = lastCell;
            int row = lastCell.getRow();
            int col = lastCell.getCol();

            // check what FieldType is at the coordinate
            FieldType cellValue = level.getGameFieldCell(row, col);

            if (cellValue == stopAt) {
                // stop when finding the field type we want to stop at
                endFound = true;
            } else {
                // if we have not been on that field before, mark it with the amount of steps to reach it
                // and continue by adding that fields neighbours to the stack
                List<Coord> adjacentCells = List.of(
                        new Coord(row + 1, col),
                        new Coord(row - 1, col),
                        new Coord(row, col - 1),
                        new Coord(row, col + 1)
                );

                // check every neighbor for more passable fields
                for (Coord cell : adjacentCells) {
                    // check the cells coordinate bounds
                    if (cell.getRow() >= 0 && cell.getRow() < levelRep.length && cell.getCol() >= 0 && cell.getCol() < levelRep[cell.getRow()].length && levelRep[cell.getRow()][cell.getCol()] == -1) {
                        FieldType currentCellType = level.getGameFieldCell(cell.getRow(), cell.getCol());
                        if (currentCellType == FieldType.NORMAL || currentCellType == FieldType.START || (currentCellType == stopAt)) {
                            // the field is a valid field to walk onto
                            cells.add(cell);
                            levelRep[cell.getRow()][cell.getCol()] = levelRep[row][col] + 1;
                        } else if (currentCellType == FieldType.GAP) {
                            // get the cell behind the gap (in the direction we are currently facing)
                            Coord cellAfterGap = new Coord(
                                    cell.getRow() + (cell.getRow() - row),
                                    cell.getCol() + (cell.getCol() - col)
                            );
                            // check if the bot is able to land on the field after the gap
                            if (cellAfterGap.getRow() >= 0 && cellAfterGap.getRow() < levelRep.length && cellAfterGap.getCol() >= 0 && cellAfterGap.getCol() < levelRep[cellAfterGap.getRow()].length && levelRep[cellAfterGap.getRow()][cellAfterGap.getCol()] == -1) {
                                FieldType cellAfterGapValue = level.getGameFieldCell(cellAfterGap.getRow(), cellAfterGap.getCol());
                                if (cellAfterGapValue == FieldType.NORMAL || cellAfterGapValue == FieldType.START || (cellAfterGapValue == stopAt)) {
                                    // the bot can land on that field
                                    levelRep[cell.getRow()][cell.getCol()] = levelRep[row][col] + 1;
                                    cells.add(cellAfterGap);
                                    levelRep[cellAfterGap.getRow()][cellAfterGap.getCol()] = levelRep[row][col] + 1;
                                }
                            }
                        }
                    }
                }
            }
        }

        // if we found the end, and it was a coin, we can move there to set that field to our new starting position
        // we also have to collect the coin so that the search for a possible next coin does not find this coin again
        if (endFound) {
            level.moveToCell(lastCellPos);
            if (level.getGameFieldCell(lastCellPos.getRow(), lastCellPos.getCol()) == FieldType.COIN) {
                level.collectCoin();
            }
        }
        return levelRep;
    }

    /**
     * Creates instructions from a given FloodFill step representation of the player, which can be used to solve the level.
     * <br><br>
     * This method is package-private for test-purposes
     * <br><br>
     *
     * @param floodFillRep array of steps from start to end, generated by applying FloodFill onto the current level
     * @param level        Level instance where the level.currentBotPosition represents the instruction destination
     * @return list of instructions to solve the level
     */
    static List<Instruction> createInstructions(Level level, int[][] floodFillRep, boolean stepOntoLast) {
        Coord currPos = level.getCurrentBotPosition();
        Coord endPos = findStartingPosition(floodFillRep);
        Deque<Coord> wayCoords = new ArrayDeque<>();
        wayCoords.add(currPos);

        // get the coordinates of all passable cells
        Coord last = wayCoords.peekLast();
        while (currPos != null && last != null && !currPos.equals(endPos)) {
            Coord nextNeighbour = null;

            List<Coord> possibleNeighbors = new ArrayList<>();
            if (level.getGameFieldCell(currPos.getRow(), currPos.getCol()) == FieldType.GAP) {
                // only add the neighbor in the current direction -> can not jump around corners
                Coord dirVec = new Coord(currPos.getRow() - last.getRow(), currPos.getCol() - last.getCol());
                possibleNeighbors.add(new Coord(currPos.getRow() + dirVec.getRow(), currPos.getCol() + dirVec.getCol()));
            } else {
                // check all four neighbors
                possibleNeighbors.add(new Coord(currPos.getRow() + 1, currPos.getCol()));
                possibleNeighbors.add(new Coord(currPos.getRow() - 1, currPos.getCol()));
                possibleNeighbors.add(new Coord(currPos.getRow(), currPos.getCol() - 1));
                possibleNeighbors.add(new Coord(currPos.getRow(), currPos.getCol() + 1));
            }

            for (Coord neighbor : possibleNeighbors) {
                // see if the neighbor coordinate is in bounds
                int row = neighbor.getRow();
                int col = neighbor.getCol();
                if (row >= 0 && row < floodFillRep.length && col >= 0 && col < floodFillRep[row].length) {
                    // get the next neighbor by finding the next smaller (or equal) number in the flood fill representation
                    if (floodFillRep[row][col] != -1 && floodFillRep[row][col] <= floodFillRep[currPos.getRow()][currPos.getCol()]
                            && !neighbor.equals(last)) {
                        nextNeighbour = neighbor;
                    }
                }
            }

            last = wayCoords.peekLast();
            if (nextNeighbour == null) {
                // Happens when the current position is a gap, and we can not jump over the gap (neighbor blocked).
                // In this case we want to go back to our previous cell and continue with a different path than the path
                // we just took (do not want to check the same gap again)
                Coord temp = last;
                last = currPos;
                currPos = temp;
            } else {
                // if we found a neighbor, and it is not a gap, we want to add it to our queue of passable coordinates
                if (level.getGameFieldCell(nextNeighbour.getRow(), nextNeighbour.getCol()) != FieldType.GAP) {
                    wayCoords.add(nextNeighbour);
                }
                currPos = nextNeighbour;
            }
        }

        currPos = wayCoords.pollLast();
        BotRotation currBotRotation = level.getBotRotation();
        List<Instruction> instructions = new ArrayList<>();

        assert currPos != null;
        while (!wayCoords.isEmpty()) {
            Coord nextPos = wayCoords.pollLast();

            // if we are not facing in the direction of the next cell, we need to add rotate instructions until the bot
            // is looking in the right direction
            if (!Coord.getNextCoord(currPos, currBotRotation).equals(nextPos)) {
                // get the new rotation to face the next cell
                BotRotation newRotation = BotRotation.getRotationChange(currPos, nextPos);

                // count how often the bot needed to turn to get the new rotation
                int turnCount = 0;
                while (currBotRotation != newRotation) {
                    currBotRotation = currBotRotation.rotateLeft();
                    level.setBotRotation(currBotRotation);
                    turnCount++;
                }

                if (turnCount > 2) {
                    // 3 turns to the left can be simplified by turning to the right once
                    instructions.add(TurnRight.getSingleton());
                } else {
                    // add one or two left turns depending on how often the bot needs to turn
                    for (int i = 0; i < turnCount; i++) {
                        instructions.add(TurnLeft.getSingleton());
                    }
                }
            }

            int diff = Coord.calculateCoordDifference(currPos, nextPos);
            if (wayCoords.size() >= 1) {
                // depending on the cell difference between the two coordinates we decide whether the bot has to walk or jump
                if (diff == 1) {
                    instructions.add(Walk.getSingleton());
                } else if (diff == 2) {
                    instructions.add(Jump.getSingleton());
                }
            } else {
                // if we want to create the instructions to reach a coin, our last instruction would be walking or jump
                // onto the coin (depends on the next field -> diff > 1 means that the next field is a gap)
                // if we want to create the instructions to reach the door, our last instruction would be to exit
                instructions.add(stepOntoLast ? (diff > 1 ? Jump.getSingleton() : Walk.getSingleton()) : Exit.getSingleton());
            }

            currPos = nextPos;
        }

        return instructions.isEmpty() ? null : instructions;
    }

    /**
     * Finds the starting position of a flood fill representation. In the flood fill representation the starting point
     * is represented as a 0.
     * <p>
     *
     * @param floodFillRep representation to find the start of
     * @return start coordinate or null if there is no start
     */
    static Coord findStartingPosition(int[][] floodFillRep) {
        Coord pos = null;

        for (int i = 0; pos == null && i < floodFillRep.length; i++) {
            for (int j = 0; pos == null && j < floodFillRep[i].length; j++) {
                pos = (floodFillRep[i][j] == 0) ? new Coord(i, j) : null;
            }
        }

        return pos;
    }

    /**
     * Converts the given instructions to program and procedure instructions which can be used in-game to solve the level.
     * <p>
     * This method is package-private for test-purposes
     *
     * @param instructions instructions to reduce into program and procedure instructions
     * @return SolverResult containing the program and procedure instructions with a SOLVED status or null for the instructions and a UNSOLVABLE status if the level is not solvable.
     */
    static SolverResult splitInstructionsToProgramAndProcedures(List<Instruction> instructions) {
        List<Instruction> programInstructions = new ArrayList<>();
        ProcedureOne procedureOne = new ProcedureOne();
        ProcedureTwo procedureTwo = new ProcedureTwo();

        if (instructions.size() <= MAX_PROGRAM_INSTRUCTIONS) {
            // no need to split the instructions into procedures
            programInstructions.addAll(instructions);
        } else {
            List<Instruction> instructionsCopy = new ArrayList<>(instructions);

            // set the first procedure to the most common sublist, replacing the full instruction list with the reference
            // of procedure one
            procedureOne.addInstructions(findMostCommonSublist(instructionsCopy));
            instructionsCopy = replaceProcedureInstructions(instructionsCopy, procedureOne);

            // set the second procedure to the second most common sublist, replacing the full instruction list with the
            // reference of procedure two
            procedureTwo.addInstructions(findMostCommonSublist(instructionsCopy));
            instructionsCopy = replaceProcedureInstructions(instructionsCopy, procedureTwo);

            programInstructions.addAll(instructionsCopy);

            // check if there are too many instructions to fit into the program instruction container
            if (programInstructions.size() > MAX_PROGRAM_INSTRUCTIONS) {
                return new SolverResult(null, null, null, SolveStatus.UNSOLVABLE_CAN_NOT_REDUCE_TO_PROGRAM_AND_PROCEDURES);
            }
        }

        return new SolverResult(programInstructions, procedureOne, procedureTwo, SolveStatus.SOLVABLE);
    }

    /**
     * Takes a list of instructions and a procedure and replaces every procedure instructions occurrence in that list
     * with a reference to the procedure itself.
     *
     * @param instructions list of instructions
     * @param p            procedure containing a set of instructions of which all occurrences in the list of instructions should
     *                     be replaced with the procedure reference itself
     * @return new list of instructions containing a procedure reference at every occurrence of the procedure instruction set
     */
    private static List<Instruction> replaceProcedureInstructions(List<Instruction> instructions, Procedure p) {
        List<Instruction> instructionsCopy = new LinkedList<>(instructions);

        // remove the most common subset from the instruction list copy
        int idxOfSublist = Collections.indexOfSubList(instructionsCopy, p.getInstructions());
        while (idxOfSublist != -1) {
            instructionsCopy.subList(idxOfSublist, idxOfSublist + p.getAmountOfInstructions()).clear();
            instructionsCopy.add(idxOfSublist, p);

            idxOfSublist = Collections.indexOfSubList(instructionsCopy, p.getInstructions());
        }
        return instructionsCopy;
    }

    /**
     * Finds the most common sub-list of a given list of instructions by comparing the occurrences of every permutation
     * ({@link #findOccurrencesOfPermutations(List)}).
     * In this case the "most common sub-list" is defined as the sub-list that replaces the most instructions in the
     * given list of instructions.
     * <p>
     * Therefore, finding the most common sub-list consists of two comparison criteria. The first one is the total amount
     * of instructions a sub-list can replace while the second one is the amount of instructions a sub-list can replace
     * at once.
     * <p>
     * This method is package-private for test-purposes
     *
     * @param instructions list of instructions to get the most common sub-list from
     * @return most common sub-list in {@code instructions}
     */
    static List<Instruction> findMostCommonSublist(List<Instruction> instructions) {
        Map<List<Instruction>, Integer> occurrences = findOccurrencesOfPermutations(instructions);

        List<Instruction> mostCommonSublist = new ArrayList<>();
        if (instructions == null || instructions.isEmpty()) {
            // empty lists do not have a sub-list
            return mostCommonSublist;
        } else if (occurrences.isEmpty()) {
            // if there is only one instruction left, then there will be no other permutations as the instruction itself
            return new ArrayList<>(instructions);
        } else {
            // create comparators to compare all the permutations and their occurrences
            Comparator<Map.Entry<List<Instruction>, Integer>> compareByTotalInstructionSize = Comparator.comparing(entry -> entry.getKey().size() * entry.getValue());
            Comparator<Map.Entry<List<Instruction>, Integer>> compareByPrecedingInstructionSize = Comparator.comparingInt(entry -> entry.getKey().size());

            // get the most common sub-list
            Optional<Map.Entry<List<Instruction>, Integer>> mostCommonPermutation = occurrences.entrySet().stream()
                    .max(compareByTotalInstructionSize.thenComparing(compareByPrecedingInstructionSize));

            // compare the largest subset with the most common one to get the most efficient subset
            if (mostCommonPermutation.isPresent()) {
                mostCommonSublist = mostCommonPermutation.get().getKey();
            }

            return mostCommonSublist;
        }

    }

    /**
     * Takes a list of instructions and counts the occurrences of every permutation found by {@link #getPermutations}.
     * <p>
     * This method is package-private for test-purposes
     *
     * @param instructions list of instructions
     * @return map of permutations (key) and their occurrences inside the specified instruction list (value)
     */
    static Map<List<Instruction>, Integer> findOccurrencesOfPermutations(List<Instruction> instructions) {
        Map<List<Instruction>, Integer> occurrences = new HashMap<>();
        List<Instruction> instructionsCopy = new ArrayList<>(instructions);
        List<List<Instruction>> permutations = getPermutations(instructionsCopy);

        for (List<Instruction> permutation : permutations) {
            int idxOfSublist = Collections.indexOfSubList(instructionsCopy, permutation);
            int occs = 0;
            while (idxOfSublist != -1) {
                occs++;
                instructionsCopy.subList(idxOfSublist, idxOfSublist + permutation.size()).clear();
                idxOfSublist = Collections.indexOfSubList(instructionsCopy, permutation);
            }
            occurrences.put(permutation, occs);
            instructionsCopy = new ArrayList<>(instructions);

        }
        return occurrences;
    }


    /**
     * Creates a list of instruction permutations of a given instruction list.
     * Different from the general definition of permutations, the list of permutations starts with a permutation size
     * of 2,and ends with a permutation size of {@value MAX_PROCEDURE_INSTRUCTIONS}.
     * As a result of these restrictions, later uses of this function have advantages in terms of their efficiency,
     * since no single-element permutations and no permutations larger than the maximum number of procedure instructions
     * have to be counted.
     * <p>
     * This method is package-private for test-purposes
     *
     * @param instructions instructions to get the permutations from
     * @return all permutations of the given instruction list within a range of 2 - {@value MAX_PROCEDURE_INSTRUCTIONS}
     */
    static List<List<Instruction>> getPermutations(List<Instruction> instructions) {
        List<List<Instruction>> permutations = new ArrayList<>();

        // finds all permutations of the size 2 to max procedure size
        for (int i = 2; i <= MAX_PROCEDURE_INSTRUCTIONS; i++) {
            List<List<Instruction>> windows = buildSlidingWindows(instructions, i);
            for (List<Instruction> pattern : windows) {
                if (!permutations.contains(pattern)) {
                    permutations.add(pattern);
                }
            }
        }

        return permutations;
    }

    /**
     * Performs the sliding window algorithm on a given list of instructions, with a given window size.
     * The sliding window algorithm starts by creating a first sub-list, from the given list, of the specified windowSize.
     * After that, it slides one element further in the list (hence the name :D) and creates another sub-list.
     * The algorithm ends, when every element of the given list is in at least one window.
     * <br><br>
     * This method is package-private for test-purposes
     * <br><br>
     *
     * @param instructions list of instructions to execute the sliding window algorithm on
     * @param windowSize   size of each sub-list (window)
     * @return list of every sub-list in the instruction list, with a size of windowSize
     */
    static List<List<Instruction>> buildSlidingWindows(List<Instruction> instructions, int windowSize) {
        List<List<Instruction>> slidingWindows = new ArrayList<>();
        for (int i = 0; i < (instructions.size() - windowSize + 1); i++) {
            int to = i + windowSize;
            List<Instruction> window = new ArrayList<>();
            for (int j = i; j < to; j++) {
                window.add(instructions.get(j));
            }
            slidingWindows.add(window);
        }
        return slidingWindows;
    }

}
