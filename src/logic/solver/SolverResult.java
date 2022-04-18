package logic.solver;

import logic.instructions.Instruction;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing the result of the LevelSolver.
 * This includes the resulting program and procedure instructions calculated by the level solving algorithm.
 *
 * @author Timo Peters
 */
public class SolverResult {
    private final List<Instruction> programInstructions;
    private final ProcedureOne p1Instructions;
    private final ProcedureTwo p2Instructions;
    private final SolveStatus solveStatus;

    /**
     * Constructor to create a solver result with the resulting program and procedure instructions and a solve status
     * indicating if the level was solvable or not.
     *
     * @param programInstructions instructions for the program container
     * @param p1                  procedure for the procedure one container
     * @param p2                  procedure for the procedure two container
     * @param solveStatus         status of the solving result
     */
    public SolverResult(List<Instruction> programInstructions, ProcedureOne p1, ProcedureTwo p2, SolveStatus solveStatus) {
        this.programInstructions = programInstructions;
        this.p1Instructions = p1;
        this.p2Instructions = p2;
        this.solveStatus = solveStatus;
    }

    /**
     * Returns a copy of the program instructions
     *
     * @return program instructions
     */
    public List<Instruction> getProgramInstructions() {
        return programInstructions != null ? new ArrayList<>(programInstructions) : null;
    }

    public ProcedureOne getP1() {
        return p1Instructions;
    }

    public ProcedureTwo getP2() {
        return p2Instructions;
    }

    public SolveStatus getSolveStatus() {
        return solveStatus;
    }
}
