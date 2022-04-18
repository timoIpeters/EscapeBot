package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import logic.conversion.Action;
import logic.exceptions.InvalidFieldSizeException;
import logic.exceptions.NoBotRotationException;
import logic.exceptions.NoFieldException;
import logic.instructions.InstructionContainer;
import logic.instructions.Procedure;
import logic.level.BotRotation;
import logic.conversion.ConversionResult;
import logic.level.FieldType;
import logic.solver.SolveStatus;
import logic.exceptions.EmptyFileException;
import logic.exceptions.InvalidBotRotationException;
import logic.exceptions.InvalidFieldTypeException;
import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.level.Coord;
import logic.level.Level;
import logic.conversion.Move;
import logic.level.SavedState;
import logic.solver.LevelSolver;
import logic.solver.SolverResult;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Logic of the game "Escape-Bot". In this game the player tries to navigate a robot
 * through a 8x8 grid from a starting position to the exit. The player needs to build a
 * program consisting of max. 12 actions and 2 procedures with max. 8 actions to solve a level.
 * There are several actions to choose from such as moving, turning, jumping and finding the exit.
 * The aim of this game is to build a program that is able to find the exit.
 *
 * @author Timo Peters
 */
public class GameLogic {

    /**
     * Connection to the gui
     */
    private final GUIConnector gui;

    /**
     * Level-Instance containing the gameField and the botRotation
     */
    private final Level level;

    /**
     * List of program instructions
     */
    private List<Instruction> programInstructions;

    /**
     * List of procedure-one instructions
     */
    private ProcedureOne procedureOne;

    /**
     * List of procedure-two instructions
     */
    private ProcedureTwo procedureTwo;

    /**
     * Maximum amount of program instructions
     */
    private final int MAX_PROGRAM_INSTRUCTIONS = 12;

    /**
     * Amount of columns every instruction container has
     */
    private final int INSTRUCTION_COLS = 4;

    /**
     * Constructor used by the GUI to initialize a default level when the UI opens.
     *
     * @param gui GUIConnector connecting the game logic to the UI
     */
    public GameLogic(GUIConnector gui) {
        this.gui = gui;
        this.level = new Level(
                new FieldType[][]{
                        {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                        {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                        {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                        {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                        {FieldType.START, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.DOOR},
                        {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                        {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL},
                        {FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL}
                },
                BotRotation.EAST
        );
        this.programInstructions = new ArrayList<>();
        this.procedureOne = new ProcedureOne();
        this.procedureTwo = new ProcedureTwo();

        gui.updateGameField(this.level.getGameField());
        gui.setBotRotation(this.level.getBotRotation());

        // update the user data on the p1 or p2 control to the given procedure reference
        gui.useProcedureOne(procedureOne);
        gui.useProcedureTwo(procedureTwo);
    }

    /**
     * Test Constructor initiating a new game with a given gameField and a default
     * botRotation of BotRotation.EAST.
     *
     * @param gui       GUIConnector connecting the game logic to the UI
     * @param gameField 2-dimensional array of FieldTypes representing the gameField
     */
    GameLogic(GUIConnector gui, FieldType[][] gameField) {
        this.gui = gui;

        this.level = new Level(
                gameField,
                BotRotation.EAST
        );
        this.programInstructions = new ArrayList<>();
        this.procedureOne = new ProcedureOne();
        this.procedureTwo = new ProcedureTwo();

        gui.updateGameField(this.level.getGameField());
        gui.setBotRotation(this.level.getBotRotation());
        gui.useProcedureOne(procedureOne);
        gui.useProcedureTwo(procedureTwo);
    }

    /**
     * Test Constructor initiating a new game with a given gameField and botRotation.
     *
     * @param gui         GUIConnector connecting the game logic to the UI
     * @param gameField   2-dimensional array of FieldTypes representing the gameField
     * @param botRotation Enum BotRotation representing the players initial line of sight.
     */
    GameLogic(GUIConnector gui, FieldType[][] gameField, BotRotation botRotation) {
        this.gui = gui;
        this.level = new Level(gameField, botRotation);
        this.programInstructions = new ArrayList<>();
        this.procedureOne = new ProcedureOne();
        this.procedureTwo = new ProcedureTwo();

        gui.updateGameField(this.level.getGameField());
        gui.setBotRotation(this.level.getBotRotation());
        gui.useProcedureOne(procedureOne);
        gui.useProcedureTwo(procedureTwo);
    }

    /**
     * Test Constructor initiating a new game with a fully preset game state including instructions for
     * program and procedures.
     *
     * @param gui                 GUIConnector connecting the game logic to the UI
     * @param gameField           2-dimensional array of FieldTypes representing the gameField
     * @param botRotation         Enum BotRotation representing the players initial line of sight.
     * @param programInstructions list of program instructions
     * @param p1                  ProcedureOne instance containing a list of instructions
     * @param p2                  ProcedureTwo instance containgin a list of instructions
     */
    GameLogic(GUIConnector gui, FieldType[][] gameField, BotRotation botRotation,
              List<Instruction> programInstructions, ProcedureOne p1, ProcedureTwo p2) {
        this.gui = gui;
        this.level = new Level(gameField, botRotation);

        this.programInstructions = programInstructions.size() <= MAX_PROGRAM_INSTRUCTIONS ? programInstructions : programInstructions.subList(0, MAX_PROGRAM_INSTRUCTIONS);
        this.procedureOne = p1;
        this.procedureTwo = p2;

        gui.updateGameField(this.level.getGameField());
        gui.setBotRotation(this.level.getBotRotation());
        gui.useProcedureOne(procedureOne);
        gui.useProcedureTwo(procedureTwo);
    }

    /**
     * Adds a program instruction. The instruction can only be added when left-clicking next to an existing instruction.
     * When clicking on an existing program instruction, it will be changed with the currently selected instruction
     *
     * @param selectedInstruction instruction to add
     * @param coord               coordinate where the instruction should be added
     */
    public void addProgramInstruction(Instruction selectedInstruction, Coord coord) {
        int lastIdx = programInstructions.size();
        int requestedIndex = Coord.convertCoordToIndex(coord, INSTRUCTION_COLS);

        // only able to append to the last instruction (until MAX_PROGRAM_INSTRUCTIONS is reached)
        if (requestedIndex == lastIdx && programInstructions.size() < MAX_PROGRAM_INSTRUCTIONS) {
            programInstructions.add(selectedInstruction);
            gui.addProgramInstruction(selectedInstruction, coord);
        } else if (requestedIndex < lastIdx) {
            // change the instruction at this position
            programInstructions.set(requestedIndex, selectedInstruction);
            gui.addProgramInstruction(selectedInstruction, coord);
        }

    }

    /**
     * Adds an instruction to procedureTwo
     *
     * @param selectedInstruction instruction to add
     * @param coord               coordinate where the instruction should be added
     */
    public void addP1Instruction(Instruction selectedInstruction, Coord coord) {
        addProcedureInstruction(selectedInstruction, coord, procedureOne, gui::addP1Instruction);
    }

    /**
     * Adds an instruction to procedureTwo
     *
     * @param selectedInstruction instruction to add
     * @param coord               coordinate where the instruction should be added
     */
    public void addP2Instruction(Instruction selectedInstruction, Coord coord) {
        addProcedureInstruction(selectedInstruction, coord, procedureTwo, gui::addP2Instruction);
    }

    /**
     * Adds a procedure instruction. The instruction can only be added when left-clicking next to an existing instruction.
     * When clicking on an existing procedure instruction, it will be changed with the currently selected instruction
     *
     * @param selectedInstruction instruction to add
     * @param coord               coordinate where the instruction should be added
     * @param procedure           procedure to add the new instruction to
     * @param guiAddFunction      BiConsumer representing the gui function to add an instruction to the specified procedure GUI container
     */
    private void addProcedureInstruction(Instruction selectedInstruction, Coord coord, Procedure procedure, BiConsumer<Instruction, Coord> guiAddFunction) {
        int amountOfInstructions = procedure.getAmountOfInstructions();
        int requestedIndex = Coord.convertCoordToIndex(coord, INSTRUCTION_COLS);

        if (requestedIndex == amountOfInstructions) {
            boolean added = procedure.addInstruction(selectedInstruction);
            if (added) {
                guiAddFunction.accept(selectedInstruction, coord);
            }
        } else if (requestedIndex < amountOfInstructions) {
            procedure.changeInstructionAtIdx(requestedIndex, selectedInstruction);
            guiAddFunction.accept(selectedInstruction, coord);
        }
    }

    /**
     * Deletes a program instruction at a given coordinate.
     *
     * @param coord coordinate of the instruction that should be deleted
     */
    public void deleteProgramInstruction(Coord coord) {
        int requestedIndex = Coord.convertCoordToIndex(coord, INSTRUCTION_COLS);
        if (requestedIndex < programInstructions.size()) {
            programInstructions.remove(requestedIndex);
            gui.deleteProgramInstruction(coord, Coord.convertIndexToCoord(programInstructions.size(), INSTRUCTION_COLS));
        }
    }

    /**
     * Deletes a procedureOne instruction at a given coordinate.
     *
     * @param coord coordinate of the instruction that should be deleted
     */
    public void deleteP1Instruction(Coord coord) {
        deleteProcedureInstruction(coord, procedureOne, gui::deleteP1Instruction);
    }

    /**
     * Deletes a procedureTwo instruction at a given coordinate.
     *
     * @param coord coordinate of the instruction that should be deleted
     */
    public void deleteP2Instruction(Coord coord) {
        deleteProcedureInstruction(coord, procedureTwo, gui::deleteP2Instruction);
    }

    /**
     * Deletes a procedure instruction at a given coordinate.
     *
     * @param coord coordinate of the instruction that should be deleted
     */
    public void deleteProcedureInstruction(Coord coord, Procedure procedure, BiConsumer<Coord, Coord> guiDeleteFunction) {
        int requestedIndex = Coord.convertCoordToIndex(coord, INSTRUCTION_COLS);
        if (requestedIndex < procedure.getAmountOfInstructions()) {
            procedure.deleteInstruction(requestedIndex);
            guiDeleteFunction.accept(coord, Coord.convertIndexToCoord(procedure.getAmountOfInstructions(), INSTRUCTION_COLS));
        }
    }

    /**
     * Deletes all program instructions
     */
    public void deleteAllProgramInstructions() {
        programInstructions.clear();
        gui.deleteAllProgramInstructions();
    }

    /**
     * Deletes all procedure 1 instructions
     */
    public void deleteAllP1Instructions() {
        procedureOne.clearInstructions();
        gui.deleteAllP1Instructions();
    }

    /**
     * deletes all procedure 2 instructions
     */
    public void deleteAllP2Instructions() {
        procedureTwo.clearInstructions();
        gui.deleteAllP2Instructions();
    }

    /**
     * Validates the current level. A level is valid if it contains exactly one start and one door.
     *
     * @return true if the level is valid
     */
    public boolean validateLevel() {
        return level.validateLevel();
    }

    /**
     * Starts the level by converting every instruction to actions and starting its animation
     */
    public void startLevel() {
        ConversionResult conversionResult = convertInstructionsToActions();
        gui.startAnimation(conversionResult, this.programInstructions.size(), this.procedureOne.getAmountOfInstructions(), this.procedureTwo.getAmountOfInstructions());
    }

    /**
     * Checks if the procedure instructions contain any recursion.
     * This includes a procedure calling itself and two procedures calling each other.
     * <p>
     * This method is package-private for test purposes.
     *
     * @return action indicating a recursion or null if there is no recursion
     */
    Action recursionInProcedures() {
        if (procedureOne.containsInstruction(procedureOne) || procedureTwo.containsInstruction(procedureTwo)) {
            return Action.LOOSE_RECURSION_PROCEDURE_CALLS_ITSELF;
        } else if (procedureOne.containsInstruction(procedureTwo) && procedureTwo.containsInstruction(procedureOne)) {
            return Action.LOOSE_RECURSION_PROCEDURES_CALL_EACH_OTHER;
        }
        return null;
    }

    /**
     * Converts the three Instruction lists to a list of actions wich are sent to the GUI
     * to handle the UI representation.
     * <p>
     * This method is package-private for test purposes.
     *
     * @return List of actions to send to the GUI
     */
    ConversionResult convertInstructionsToActions() {
        // check for a recursion error
        Action recursionError = recursionInProcedures();
        if (recursionError != null) {
            Move move = new Move(level.getCurrentBotPosition(), level.getBotRotation(), recursionError, List.of(InstructionContainer.RESULT));
            return new ConversionResult(List.of(move));
        }

        // work on a level copy to track the current level state
        Level levelCopy = level.getLevelCopy();

        int[] lostCount = new int[]{0};
        List<Move> moves = programInstructions.stream()
                .flatMap(instruction -> instruction.convertInstruction(levelCopy, List.of(InstructionContainer.PROGRAM)))
                .takeWhile(move -> {
                    // inclusive takeWhile using a lostCount
                    // this will also take the first value where the predicate does not match
                    if (move.getInstructionContainer().equals(List.of(InstructionContainer.RESULT))) ++lostCount[0];
                    return lostCount[0] < 2;
                })
                .collect(Collectors.toList());


        // Because I am working with streams, where a streamed value does not know what comes before or after it, this
        // extra part is needed to determine whether there were instructions after the first exit call or not
        if (moves.size() == 0) {
            //there are no actions
            moves.add(new Move(levelCopy.getCurrentBotPosition(), levelCopy.getBotRotation(), Action.LOOSE_NO_ACTIONS, List.of(InstructionContainer.RESULT)));
        } else {
            // find the first exit and save its index into i
            boolean exitInThere = false;
            int i = 0;
            while (!exitInThere && i < moves.size()) {
                exitInThere = moves.get(i++).getAction() == Action.EXIT;
            }

            if (exitInThere) {
                // check if exit was the last called instruction
                boolean lastExit = (moves.size()) == i;
                if (!lastExit) {
                    moves.subList(i - 1, moves.size()).clear();

                    // there were other instructions after the exit
                    moves.add(new Move(levelCopy.getCurrentBotPosition(), levelCopy.getBotRotation(), Action.LOOSE_INSTRUCTIONS_AFTER_EXIT, List.of(InstructionContainer.RESULT)));
                } else {
                    // last action was exit
                    moves.add(new Move(levelCopy.getCurrentBotPosition(), levelCopy.getBotRotation(), Action.WIN, List.of(InstructionContainer.RESULT)));
                }
            } else if (!moves.get(moves.size() - 1).getInstructionContainer().equals(List.of(InstructionContainer.RESULT))) {
                // not ending with exit
                moves.add(new Move(levelCopy.getCurrentBotPosition(), levelCopy.getBotRotation(), Action.LOOSE_NOT_ENDING_WITH_EXIT, List.of(InstructionContainer.RESULT)));
            }
        }
        return new ConversionResult(moves);
    }

    /**
     * updates the amount of coins in the level
     */
    public void updateCoins() {
        this.level.countCoins();
    }

    /**
     * Changes the current field type at a specified coordinate to the new field type given as a parameter
     * and informs the GUI that a field changed.
     *
     * @param coord        coordinate in the game field
     * @param newFieldType new field type
     */
    public void changeFieldType(Coord coord, FieldType newFieldType) {
        this.level.setGameFieldCell(newFieldType, coord);
        gui.changeFieldType(coord, newFieldType);
        if (newFieldType == FieldType.START) {
            gui.setBotRotation(this.level.getBotRotation());
        }
    }

    /**
     * rotates the bot to the right
     */
    public void rotateBot() {
        this.level.rotateBotRight();
        gui.rotateBot();
    }

    /**
     * Resets all occurrences of a field to the default field type (FieldType.NORMAL).
     *
     * @param fieldType field type to reset all occurrences of
     */
    public void resetAllOccurrencesOfField(FieldType fieldType) {
        List<Coord> occurrences = this.level.allOccurrencesOfField(fieldType);
        if (occurrences != null) {
            for (Coord occurrence : occurrences) {
                this.level.setGameFieldCell(FieldType.NORMAL, occurrence);
                gui.resetField(occurrence);
            }
        }
    }

    /**
     * Creates an empty level
     */
    public void newLevel() {
        Level level = new Level(
                new FieldType[][]{
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,},
                        {FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL, FieldType.NORMAL,}
                },
                BotRotation.NORTH);
        initializeGameField(level);
    }

    public void solveLevel() {
        SolverResult solverResult = LevelSolver.solve(level);

        if (solverResult.getSolveStatus() == SolveStatus.SOLVABLE) {
            // replace the current program and procedure 1 & 2 instructions with the ones from the solver result
            programInstructions = solverResult.getProgramInstructions();
            procedureOne = solverResult.getP1();
            procedureTwo = solverResult.getP2();
            gui.useProcedureOne(procedureOne);
            gui.useProcedureTwo(procedureTwo);

            // add the new instructions to the gui
            gui.fillProgramInstructions(programInstructions);
            gui.fillP1Instructions(procedureOne.getInstructions());
            gui.fillP2Instructions(procedureTwo.getInstructions());
        }

        // tell the user if the level was solvable
        gui.showLevelSolverMessage(solverResult.getSolveStatus());
    }

    /**
     * Loads a level from a given file.
     * <p>
     *
     * @param file given level file in JSON format
     */
    public void loadLevelFromFile(File file) {
        assert file != null;
        try (Reader r = new FileReader(file)) {
            loadLevelFromReader(r);
        } catch (IOException e) {
            gui.showErrorMessage(e);
        }
    }

    /**
     * Utility function which loads a level from a given reader.
     *
     * @param reader reader to read the file with
     */
    public void loadLevelFromReader(Reader reader) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            SavedState newLevel = gson.fromJson(reader, SavedState.class);
            if (newLevel != null) {
                initializeGameField(newLevel.getLevel());
            } else {
                throw new EmptyFileException();
            }
        } catch (JsonParseException | EmptyFileException | InvalidBotRotationException | InvalidFieldTypeException
                | InvalidFieldSizeException | NoFieldException | NoBotRotationException e) {
            gui.showErrorMessage(e);
        }
    }


    /**
     * Initializes the current level with a given level state.
     * <p>
     * This method is package-private for test purposes ONLY.
     *
     * @param newLevel given level to change the current level to
     */
    void initializeGameField(Level newLevel) {
        //  change gameField according to the given level data
        this.level.setGameField(newLevel.getGameField());
        this.level.setBotRotation(newLevel.getBotRotation());
        this.level.countCoins();

        //  update UI gameField
        gui.updateGameField(this.level.getGameField());
        gui.setBotRotation(this.level.getBotRotation());
    }

    /**
     * Parses the current level into a JSON object and saves it to the given file.
     * <p>
     *
     * @param file file to save the level to
     */
    public void saveLevelToFile(File file) {
        if (file == null) return;

        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(level.createSavedState(), writer);
        } catch (IOException e) {
            gui.showErrorMessage(e);
        }
    }

    public void reset() {
        this.gui.updateGameField(level.getGameField());
        this.gui.setBotRotation(level.getBotRotation());
    }

    /**
     * Level getter for test purposes ONLY (therefore package-private)
     *
     * @return a copy of the current GameLogic level
     */
    Level getLevel() {
        return this.level.getLevelCopy();
    }

    /**
     * Program instructions getter for test purposes ONLY (therefore package-private)
     *
     * @return a copy of the current GameLogic program instructions
     */
    List<Instruction> getProgramInstructions() {
        return new ArrayList<>(programInstructions);
    }

    /**
     * ProcedureOne instructions getter for test purposes ONLY (therefore package-private)
     *
     * @return a copy of the current GameLogic procedureOne instructions
     */
    List<Instruction> getProcedureOneInstructions() {
        return procedureOne.getInstructions();
    }

    /**
     * ProcedureTwo instructions getter for test purposes ONLY (therefore package-private)
     *
     * @return a copy of the current GameLogic procedureTwo instructions
     */
    List<Instruction> getProcedureTwoInstructions() {
        return procedureTwo.getInstructions();
    }

    public Coord getBotPosition() {
        return this.level.getCurrentBotPosition();
    }
}
