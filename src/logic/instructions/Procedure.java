package logic.instructions;

import logic.conversion.Action;
import logic.conversion.Move;
import logic.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Abstract class for all procedures. This class already contains default implementations
 * all procedures have in common.
 *
 * @author Timo Peters
 */
public abstract class Procedure implements Instruction {

    /**
     * List of instructions
     */
    private final List<Instruction> procedure;

    /**
     * Maximum amount of instructions that fit into one procedure
     */
    private final int MAX_PROCEDURE_INSTRUCTIONS = 8;

    /**
     * Constructor to create an empty procedure.
     */
    public Procedure() {
        this.procedure = new ArrayList<>();
    }

    /**
     * Constructor to create a prefilled procedure from a given list of instructions.
     *
     * @param instructions list of instructions to directly fill the procedure with
     */
    public Procedure(List<Instruction> instructions) {
        this.procedure = instructions;
    }

    @Override
    public Stream<Move> convertInstruction(Level level, List<InstructionContainer> instructionContainer) {
        List<InstructionContainer> instructionStack = new ArrayList<>(instructionContainer);
        instructionStack.add(this.getInstructionContainer());

        // check if the procedure is filled with any instructions
        if (!this.procedure.isEmpty()) {
            int[] lostCount = new int[]{0};

            return this.procedure.stream().flatMap(i -> i.convertInstruction(level, Collections.unmodifiableList(instructionStack)))
                    .takeWhile(move -> {
                                // inclusive takeWhile using a lostCount
                                // this will also take the first value where the predicate does not match
                                if (move.getInstructionContainer().equals(List.of(InstructionContainer.RESULT))) lostCount[0]++;
                                return lostCount[0] < 2;
                            }
                    );
        } else {
            // if there was no instruction inside the procedure, then an Empty_Procedure action should be returned to ensure
            // that the position of the blue animation frame in the logic is being incremented correctly
            return Stream.of(new Move(level.getCurrentBotPosition(), level.getBotRotation(), Action.EMPTY_PROCEDURE, instructionContainer));
        }
    }

    /**
     * Returns the instruction container of the procedure. This is a different container for every existing procedure.
     *
     * @return instruction container of the specific procedure
     */
    protected abstract InstructionContainer getInstructionContainer();

    /**
     * Checks if the given instruction is in the procedure.
     *
     * @param instruction instruction to check
     * @return true if the procedure contains the instruction
     */
    public boolean containsInstruction(Instruction instruction) {
        return procedure.contains(instruction);
    }

    /**
     * Adds a given instruction to the procedure. This only works if the procedure is not full.
     *
     * @param instruction instruction to add
     * @return true if the instruction could be added, false if the procedure's instruction limit is reached
     */
    public boolean addInstruction(Instruction instruction) {
        if (procedure.size() < MAX_PROCEDURE_INSTRUCTIONS) {
            procedure.add(instruction);
            return true;
        }
        return false;
    }

    public boolean addInstructions(List<Instruction> instructions) {
        if ((procedure.size() + instructions.size()) <= MAX_PROCEDURE_INSTRUCTIONS) {
            procedure.addAll(instructions);
            return true;
        }
        return false;
    }

    /**
     * Removes an instruction by a given index from the procedure.
     *
     * @param idx index of the instruction that should be deleted from the procedure
     */
    public void deleteInstruction(int idx) {
        procedure.remove(idx);
    }

    /**
     * Changes an instruction at a given index to a new instruction.
     *
     * @param idx         index of the instruction that should be overridden
     * @param instruction instruction to override the old instruction with
     */
    public void changeInstructionAtIdx(int idx, Instruction instruction) {
        procedure.set(idx, instruction);
    }

    /**
     * Removes every instruction from the procedure
     */
    public void clearInstructions() {
        this.procedure.clear();
    }

    public int getAmountOfInstructions() {
        return procedure.size();
    }

    public List<Instruction> getInstructions() {
        return new ArrayList<>(procedure);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Procedure procedure1 = (Procedure) o;
        return procedure.equals(procedure1.procedure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(procedure, MAX_PROCEDURE_INSTRUCTIONS);
    }
}
