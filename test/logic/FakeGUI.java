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
 * Fake GUI for testing purposes. Acts like a GUIConnector without any implemented interactions.
 *
 * @author Timo Peters
 */
public class FakeGUI implements GUIConnector {

    @Override
    public void updateGameField(FieldType[][] gameField) {

    }

    public void setBotRotation(BotRotation rotation) {

    }

    @Override
    public void changeFieldType(Coord coord, FieldType newFieldType) {

    }

    @Override
    public void rotateBot() {

    }

    @Override
    public void resetField(Coord fieldCoord) {

    }

    @Override
    public void startAnimation(ConversionResult conversionResult, int programLength, int p1Length, int p2Length) {

    }

    @Override
    public void showErrorMessage(Exception e) {

    }

    @Override
    public void showLevelSolverMessage(SolveStatus solvable) {

    }

    @Override
    public void useProcedureOne(ProcedureOne p1) {

    }

    @Override
    public void useProcedureTwo(ProcedureTwo p2) {

    }

    @Override
    public void addProgramInstruction(Instruction selectedInstruction, Coord coord) {

    }

    @Override
    public void fillProgramInstructions(List<Instruction> instructions) {

    }

    @Override
    public void addP1Instruction(Instruction selectedInstruction, Coord coord) {

    }

    @Override
    public void fillP1Instructions(List<Instruction> instructions) {

    }

    @Override
    public void addP2Instruction(Instruction selectedInstruction, Coord coord) {

    }

    @Override
    public void fillP2Instructions(List<Instruction> instructions) {

    }

    @Override
    public void deleteProgramInstruction(Coord deleteCoord, Coord lastCoord) {

    }

    @Override
    public void deleteP1Instruction(Coord deleteCoord, Coord lastCoord) {

    }

    @Override
    public void deleteP2Instruction(Coord deleteCoord, Coord lastCoord) {

    }

    @Override
    public void deleteAllProgramInstructions() {

    }

    @Override
    public void deleteAllP1Instructions() {

    }

    @Override
    public void deleteAllP2Instructions() {

    }
}
