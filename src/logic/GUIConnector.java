package logic;

import logic.level.BotRotation;
import logic.conversion.ConversionResult;
import logic.level.FieldType;
import logic.solver.SolveStatus;
import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.level.Coord;

import java.util.List;

/**
 * Interface used for the logic of the game to communicate with the gui.
 *
 * @author Timo Peters
 */
public interface GUIConnector {

    /**
     * Updates the game field GUI into the game field given as a parameter
     *
     * @param gameField new game field
     */
    void updateGameField(FieldType[][] gameField);

    /**
     * Sets the bot rotation, on the game field, specified in the logic
     *
     * @param rotation initial bot rotation
     */
    void setBotRotation(BotRotation rotation);

    /**
     * Changes the Image at a specified coordinate in the UI game grid to the new field type.
     *
     * @param coord        coordinate of the field to change
     * @param newFieldType new field type
     */
    void changeFieldType(Coord coord, FieldType newFieldType);

    /**
     * Rotates the bot to the right
     */
    void rotateBot();

    /**
     * Resets a field at a given coordinate to the default field type (FieldType.NORMAL)
     *
     * @param fieldCoord coordinate of the cell that should be reset
     */
    void resetField(Coord fieldCoord);

    /**
     * Starts the level animation and gives a game result when the animation finished
     *
     * @param conversionResult list of actions to animate
     * @param programLength    amount of program instructions
     * @param p1Length         amount of procedure one instructions
     * @param p2Length         amount of procedure two instructions
     */
    void startAnimation(ConversionResult conversionResult, int programLength, int p1Length, int p2Length);

    /**
     * Shows an error message to the screen. The specific error message shown is selected by the given exception.
     *
     * @param e exception to show on the screen
     */
    void showErrorMessage(Exception e);

    /**
     * Shows a message with a given message type to the screen.
     *
     * @param solvable SolveStatus enum value indicating if the level was solvable or not. If it was not solvable then
     *                 the SolveStatus will be a detailed status of why the level was not solvable.
     */
    void showLevelSolverMessage(SolveStatus solvable);

    /**
     * Specifies the instance that should be used when adding instructions to procedure 1
     *
     * @param p1 procedure 1 instance to use
     */
    void useProcedureOne(ProcedureOne p1);

    /**
     * Specifies the instance that should be used when adding instructions to procedure 2
     *
     * @param p2 procedure 2 isntance to use
     */
    void useProcedureTwo(ProcedureTwo p2);

    /**
     * Adds a program instruction to the given coordinate in the program instruction container
     *
     * @param selectedInstruction instruction to add
     * @param coord               coordinate where to add the instruction
     */
    void addProgramInstruction(Instruction selectedInstruction, Coord coord);

    /**
     * Adds all the given instructions to the program instruction container, starting at the first cell
     *
     * @param instructions instructions to add
     */
    void fillProgramInstructions(List<Instruction> instructions);

    /**
     * Adds a procedure instruction to the given coordinate in the procedure 1 instruction container
     *
     * @param selectedInstruction instruction to add
     * @param coord               coordinate where to add the instruction
     */
    void addP1Instruction(Instruction selectedInstruction, Coord coord);

    /**
     * Adds all the given instructions to the procedure1 instruction container, starting at the first cell
     *
     * @param instructions instructions to add
     */
    void fillP1Instructions(List<Instruction> instructions);

    /**
     * Adds a procedure instruction to the given coordinate in the procedure 2 instruction container
     *
     * @param selectedInstruction instruction to add
     * @param coord               coordinate where to add the instruction
     */
    void addP2Instruction(Instruction selectedInstruction, Coord coord);

    /**
     * Adds all the given instructions to the procedure2 instruction container, starting at the first cell
     *
     * @param instructions instructions to add
     */
    void fillP2Instructions(List<Instruction> instructions);

    /**
     * Deletes a program instruction at a given coordinate in the program instruction container.
     * To see if the deletion is possible, the last instruction coordinate of the container is given as a parameter as well.
     *
     * @param deleteCoord coordinate of the instruction that should be deleted
     * @param lastCoord   coordinate of the last instruction in the instruction container
     */
    void deleteProgramInstruction(Coord deleteCoord, Coord lastCoord);

    /**
     * Deletes a procedure instruction at a given coordinate in the procedure 1 instruction container.
     * To see if the deletion is possible, the last instruction coordinate of the container is given as a parameter as well.
     *
     * @param deleteCoord coordinate of the instruction that should be deleted
     * @param lastCoord   coordinate of the last instruction in the instruction container
     */
    void deleteP1Instruction(Coord deleteCoord, Coord lastCoord);

    /**
     * Deletes a procedure instruction at a given coordinate in the procedure 2 instruction container.
     * To see if the deletion is possible, the last instruction coordinate of the container is given as a parameter as well.
     *
     * @param deleteCoord coordinate of the instruction that should be deleted
     * @param lastCoord   coordinate of the last instruction in the instruction container
     */
    void deleteP2Instruction(Coord deleteCoord, Coord lastCoord);

    /**
     * Deletes all program instruction from the program instruction container
     */
    void deleteAllProgramInstructions();

    /**
     * Deletes all procedure instructions from the procedure 1 instruction container
     */
    void deleteAllP1Instructions();

    /**
     * Deletes all procedure instructions from the procedure 2 instruction container
     */
    void deleteAllP2Instructions();
}
