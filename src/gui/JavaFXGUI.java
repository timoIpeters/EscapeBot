package gui;

import com.google.gson.JsonParseException;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.conversion.Move;
import logic.conversion.Action;
import logic.exceptions.InvalidFieldSizeException;
import logic.exceptions.NoBotRotationException;
import logic.exceptions.NoFieldException;
import logic.instructions.InstructionContainer;
import logic.level.BotRotation;
import logic.conversion.ConversionResult;
import logic.solver.SolveStatus;
import logic.exceptions.EmptyFileException;
import logic.exceptions.InvalidBotRotationException;
import logic.exceptions.InvalidFieldTypeException;
import logic.instructions.Exit;
import logic.instructions.Instruction;
import logic.instructions.Jump;
import logic.instructions.ProcedureOne;
import logic.instructions.ProcedureTwo;
import logic.instructions.TurnLeft;
import logic.instructions.TurnRight;
import logic.instructions.Walk;
import logic.level.Coord;
import logic.level.FieldType;
import logic.GUIConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is responsible for changing the gui when the logic deems it
 * necessary. Created by the gui and then passed as a parameter into the logic.
 *
 * @author Timo Peters
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * Amount of columns every instruction container has
     */
    private static final int INSTRUCTION_COLS = 4;

    /**
     * UI game grid
     */
    private final ImageView[][] gameFields;

    /**
     * Images of the current program instructions
     */
    private final ImageView[][] programFields;

    /**
     * Images of the current procedure 1 instructions
     */
    private final ImageView[][] p1Fields;

    /**
     * Images of the current procedure 2 instructions
     */
    private final ImageView[][] p2Fields;

    /**
     * ImageView for procedure 1 containing the ProcedureOne instance of the game logic
     */
    private final ImageView p1Control;

    /**
     * ImageView for procedure 2 containing the ProcedureTwo instance of the game logic
     */
    private final ImageView p2Control;

    /**
     * Reference to the parent UI element of the game scene
     */
    private final BorderPane borderPane;

    /**
     * Button to start the level;
     */
    private final Button startBtn;

    /**
     * List of multiple nodes on the right program half that should be disabled when running the game
     * This list includes the toggle mode button and the complete program container (including program and procedure
     * containers with their delete buttons)
     */
    private final ArrayList<Node> righthalfDisableGroup;

    /**
     * Reference to the level menu tab at the top of the UI
     */
    private final Menu levelMenu;

    /**
     * Message object to create different message dialogs
     */
    private final Message message;

    /**
     * Grid representing the game field
     */
    private final GridPane gameGrid;

    /**
     * Coordinate of the current player position
     */
    private Coord playerCoord;

    /**
     * List of available field images (field types)
     */
    private final List<Image> AVAILABLE_FIELD_IMAGES = new ArrayList<>();

    /**
     * Map consisting of the instructions string representation as key and the corresponding image as value
     */
    private final Map<String, Image> AVAILABLE_INSTRUCTION_IMAGES = new HashMap<>();

    /**
     * Array containing all the different instructions that are available in the game
     */
    private static final Instruction[] AVAILABLE_INSTRUCTIONS = new Instruction[]{
            Walk.getSingleton(),
            TurnLeft.getSingleton(),
            TurnRight.getSingleton(),
            Jump.getSingleton(),
            Exit.getSingleton(),
            new ProcedureOne(),
            new ProcedureTwo()
    };

    /**
     * running animation
     */
    private Animation animation = null;

    /**
     * Player image used in the running {@link #animation}
     */
    private ImageView player;

    /**
     * Image of the currently running program instruction
     */
    private ImageView currentInstructionProgramImage;

    /**
     * Image of the currently running procedure one instruction
     */
    private ImageView currentInstructionP1Image;

    /**
     * Image of the currently running procedure two instruction
     */
    private ImageView currentInstructionP2Image;

    /**
     * Current game speed
     */
    private GameSpeed speed;

    /**
     * Constructor to create a JAVAFXGui instance.
     * The constructor takes every needed UI component to properly display UI changes as parameter.
     *
     * @param gameFields            game fields
     * @param programFields         program fields
     * @param p1Fields              procedure 1 fields
     * @param p2Fields              procedure 2 fields
     * @param p1Control             procedure 1 control image
     * @param p2Control             procedure 2 control image
     * @param message               message object to open message dialogs
     * @param borderPane            Parent UI element
     * @param levelMenu             level menu tab
     * @param startBtn              button so start/stop the animation
     * @param rightHalfDisableGroup group of UI elements from the right border pane half
     * @param gameGrid              gridPane of the game field
     * @param speed                 animation speed
     */
    public JavaFXGUI(ImageView[][] gameFields, ImageView[][] programFields,
                     ImageView[][] p1Fields, ImageView[][] p2Fields, ImageView p1Control, ImageView p2Control, Message message,
                     BorderPane borderPane, Menu levelMenu, Button startBtn, ArrayList<Node> rightHalfDisableGroup,
                     GridPane gameGrid, GameSpeed speed) {
        this.gameFields = gameFields;
        this.gameGrid = gameGrid;
        this.programFields = programFields;
        this.p1Fields = p1Fields;
        this.p2Fields = p2Fields;
        this.p1Control = p1Control;
        this.p2Control = p2Control;
        this.message = message;
        this.borderPane = borderPane;
        this.levelMenu = levelMenu;
        this.startBtn = startBtn;
        this.righthalfDisableGroup = rightHalfDisableGroup;
        this.currentInstructionProgramImage = programFields[0][0];
        this.currentInstructionP1Image = p1Fields[0][0];
        this.currentInstructionP2Image = p2Fields[0][0];
        this.speed = speed;

        // fill available field images once to reuse them later when the UI changes
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType == FieldType.START) {
                this.AVAILABLE_FIELD_IMAGES.add(
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/img/Player.png")))
                );
            } else {
                this.AVAILABLE_FIELD_IMAGES.add(
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/img/" + fieldType.toString() + ".png")))
                );
            }
        }

        // fill available instruction images once to reuse them later when the UI changes
        for (Instruction ins : AVAILABLE_INSTRUCTIONS) {
            this.AVAILABLE_INSTRUCTION_IMAGES.put(
                    ins.toString(),
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/img/" + ins + ".png")))
            );
        }
    }

    @Override
    public void updateGameField(FieldType[][] gameField) {
        for (int row = 0; row < this.gameFields.length; row++) {
            for (int col = 0; col < this.gameFields[row].length; col++) {
                Coord currCoord = new Coord(row, col);
                changeFieldType(currCoord, gameField[row][col]);

                if (gameField[row][col] == FieldType.START) {
                    playerCoord = currCoord;
                }
            }
        }
    }

    @Override
    public void setBotRotation(BotRotation rotation) {
        ImageView playerImageView = gameFields[playerCoord.getRow()][playerCoord.getCol()];
        playerImageView.setRotate(rotation.getRotation());
    }

    @Override
    public void changeFieldType(Coord coord, FieldType newFieldType) {
        gameFields[coord.getRow()][coord.getCol()].setRotate(0);
        gameFields[coord.getRow()][coord.getCol()].setImage(
                AVAILABLE_FIELD_IMAGES.get(newFieldType.ordinal())
        );
        if (newFieldType == FieldType.START) {
            playerCoord = coord;
        }
    }

    @Override
    public void rotateBot() {
        ImageView botImage = gameFields[playerCoord.getRow()][playerCoord.getCol()];
        botImage.setRotate(botImage.getRotate() + 90);
    }

    @Override
    public void resetField(Coord fieldCoord) {
        ImageView fieldImage = gameFields[fieldCoord.getRow()][fieldCoord.getCol()];
        fieldImage.setImage(AVAILABLE_FIELD_IMAGES.get(FieldType.NORMAL.ordinal()));
    }

    /**
     * Creates an animation depending on the given action.
     *
     * @param move move to animate (contains the action, current bot position and current bot rotation)
     * @return created animation of that action
     */
    private Animation createAnimationFromAction(Move move, Node player, double moveLength, double speed) {
        switch (move.getAction()) {
            case WALK:
                return createWalkTransition(move.getCurrentBotRotation(), player, moveLength, speed);
            case TURN_LEFT:
                return createTurnTransition(move.getCurrentBotRotation(), player, speed, -90);
            case TURN_RIGHT:
                return createTurnTransition(move.getCurrentBotRotation(), player, speed, +90);
            case COLLECT_COIN:
                return createRemoveCoinTransition(move);
            case EXIT:
                return createRemoveDoorTransition(move);
            case JUMP_OVER:
                return createJumpTransition(move.getCurrentBotRotation(), player, moveLength * 2, speed);
            default:
                return new TranslateTransition(Duration.millis(1), player);
        }
    }

    /**
     * Creates a TranslateTransition for the walk animation.
     *
     * @param botDir     the bots' walking direction
     * @param player     player node that should be animated
     * @param moveLength length the player should move
     * @param speed      current game speed
     * @return walk animation (TranslateTransition)
     */
    private TranslateTransition createWalkTransition(BotRotation botDir, Node player, double moveLength, double speed) {
        TranslateTransition walk = new TranslateTransition(Duration.millis(speed), player);

        if (botDir.isHorizontal()) {
            walk.setByX(moveLength * (botDir.getDirectionVector().getRow() + botDir.getDirectionVector().getCol()));
        } else {
            walk.setByY(moveLength * (botDir.getDirectionVector().getRow() + botDir.getDirectionVector().getCol()));
        }
        return walk;
    }

    /**
     * Creates a RotateTransition for the turn left/right animation.
     *
     * @param botDir the bots' walking direction
     * @param player player node that should be animated
     * @param speed  current game speed
     * @param angle  rotation angle
     * @return turn animation (RotateTransition)
     */
    private RotateTransition createTurnTransition(BotRotation botDir, Node player, double speed, int angle) {
        RotateTransition rotateLeft = new RotateTransition(Duration.millis(speed), player);
        rotateLeft.setFromAngle(botDir.getRotation());
        rotateLeft.setByAngle(angle);
        return rotateLeft;
    }

    /**
     * Removes the door from the current game field. As a part of the animation this is realized as a PauseTransition.
     *
     * @param move reference to the current move
     * @return door remove animation (PauseTransition)
     */
    private PauseTransition createRemoveDoorTransition(Move move) {
        Coord nextCell = Coord.getNextCoord(move.getCurrentBotPosition(), move.getCurrentBotRotation());
        ImageView cellImage = gameFields[nextCell.getRow()][nextCell.getCol()];
        PauseTransition cellTransition = new PauseTransition(Duration.ONE);
        cellTransition.onFinishedProperty().set(
                value -> cellImage.setImage(AVAILABLE_FIELD_IMAGES.get(FieldType.NORMAL.ordinal()))
        );
        return cellTransition;
    }

    /**
     * Removes a coin from the current game field. As a part of the animation this is realized as a PauseTransition.
     *
     * @param move reference to the current move
     * @return coin remove animation (PauseTransition)
     */
    private PauseTransition createRemoveCoinTransition(Move move) {
        Coord currPos = move.getCurrentBotPosition();
        ImageView cellImage = gameFields[currPos.getRow()][currPos.getCol()];
        PauseTransition cellTransition = new PauseTransition(Duration.ONE);
        cellTransition.onFinishedProperty().set(
                value -> cellImage.setImage(AVAILABLE_FIELD_IMAGES.get(FieldType.NORMAL.ordinal()))
        );
        return cellTransition;
    }

    /**
     * Creates a ParallelTransition for the jump animation. The jump animation is realized as a parallel running
     * translate and scaling effect to point out the difference between the walk and the jump animation.
     *
     * @param botDir     the bots' walking direction
     * @param player     player node that should be animated
     * @param moveLength length the player should jump
     * @param speed      current game speed
     * @return jump animation (ParallelTransition [TranslateTransition and ScaleTransition])
     */
    private ParallelTransition createJumpTransition(BotRotation botDir, Node player, double moveLength, double speed) {
        TranslateTransition walkTransition = createWalkTransition(botDir, player, moveLength, speed);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(speed / 2), player);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        return new ParallelTransition(walkTransition, scaleTransition);
    }

    /**
     * Creates an animation delay which will be added after every animated action.
     *
     * @return pause animation used as a delay between animated actions
     */
    private Animation createAnimationDelay(double speed) {
        Duration pauseDuration = (speed == 0)
                ? Duration.ONE
                : Duration.millis(speed / 4);
        return new PauseTransition(pauseDuration);
    }

    /**
     * Opens a message dialog with the game result depending on the given action.
     * This action is the last action of the list of actions and therefore indicates whether the user wins or looses.
     *
     * @param action action indicating the game result
     */
    private void showGameResult(Action action) {
        switch (action) {
            case WIN:
                message.openMessageDialog(message.getLanguageBundleString("msg.win"), MessageType.INFORMATION);
                break;
            case LOOSE_NO_ACTIONS:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.no-actions"), MessageType.WARNING);
                break;
            case LOOSE_DOOR_OUT_OF_REACH:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.door-out-of-reach"), MessageType.WARNING);
                break;
            case LOOSE_NEXT_FIELD_BLOCKED:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.next-field-blocked"), MessageType.WARNING);
                break;
            case LOOSE_CAN_NOT_JUMP:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.can-not-jump"), MessageType.WARNING);
                break;
            case LOOSE_CAN_NOT_LAND:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.can-not-land"), MessageType.WARNING);
                break;
            case LOOSE_UNCOLLECTED_COINS:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.uncollected-coins"), MessageType.WARNING);
                break;
            case LOOSE_NOT_ENDING_WITH_EXIT:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.not-ending-with-exit"), MessageType.WARNING);
                break;
            case LOOSE_INSTRUCTIONS_AFTER_EXIT:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.instructions-after-exit"), MessageType.WARNING);
                break;
            case LOOSE_RECURSION_PROCEDURE_CALLS_ITSELF:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.recursion.procedure-calls-itself"), MessageType.ERROR);
                break;
            case LOOSE_RECURSION_PROCEDURES_CALL_EACH_OTHER:
                message.openMessageDialog(message.getLanguageBundleString("msg.loose.recursion.procedures-call-each-other"), MessageType.ERROR);
                break;
        }
    }

    /**
     * Marks a single instruction that is currently running with a small blue frame.
     *
     * @param instructionIndex         index of the currently running instruction inside the instruction container
     * @param fields                   instruction container
     * @param previousInstructionImage reference to the instruction image that was framed before
     * @return currently framed instruction image
     */
    private static ImageView markSingleInstruction(int instructionIndex, ImageView[][] fields, ImageView previousInstructionImage) {
        previousInstructionImage.setEffect(null);
        Coord coord = Coord.convertIndexToCoord(instructionIndex, INSTRUCTION_COLS);

        if (instructionIndex != -1) {
            ImageView currentImage = fields[coord.getRow()][coord.getCol()];
            currentImage.setEffect(new InnerShadow(20.0, Color.BLUE));
            return currentImage;
        }

        return previousInstructionImage;
    }

    @Override
    public void startAnimation(ConversionResult conversionResult, int programLength, int p1Length, int p2Length) {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setResizable(false);
        righthalfDisableGroup.forEach(node -> node.setDisable(true));
        levelMenu.setDisable(true);

        // disable the start/stop button if no instructions were entered
        // otherwise you could stop a level that has never even started
        startBtn.setDisable(conversionResult.getMoves().size() == 1);

        List<Move> movesCopy = conversionResult.getMoves();
        SequentialTransition animations = new SequentialTransition();

        ImageView gridPlayer = gameFields[playerCoord.getRow()][playerCoord.getCol()];

        // set the player animation image to be borderless
        player = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/img/PlayerTransparent.png")))
        );
        player.fitWidthProperty().bind(gameGrid.widthProperty().divide(gameGrid.getColumnCount()));
        player.fitHeightProperty().bind(gameGrid.heightProperty().divide(gameGrid.getRowCount()));
        player.setRotate(gridPlayer.getRotate());

        // index of the specific instruction container cell that should be highlighted
        // in the beginning no instruction should be highlighted
        int[] instructionIndex = {-1, -1, -1};
        int[] lengths = {programLength, p1Length, p2Length};
        List<InstructionContainer> prevInstructionsContainers = new ArrayList<>();

        // add animations for every move with the correct blue frame surrounding the current instruction
        for (int i = 0; i < movesCopy.size() && !movesCopy.get(i).getInstructionContainer().equals(List.of(InstructionContainer.RESULT)); i++) {
            Move currentMove = movesCopy.get(i);

            // the COLLECT_COIN action does not need an animated blue frame
            if (currentMove.getAction() != Action.COLLECT_COIN) {
                List<InstructionContainer> container = currentMove.getInstructionContainer();

                // find the index where the prev and current container differ
                int diffIdx = -1;
                for (int j = 0; diffIdx == -1; j++) {
                    if (j == prevInstructionsContainers.size() || j == container.size() || prevInstructionsContainers.get(j) != container.get(j)) {
                        diffIdx = j;
                    }
                }

                // remove containers that are not on the stack anymore
                for (int j = prevInstructionsContainers.size() - 1; j >= diffIdx; j--) {
                    InstructionContainer removed = prevInstructionsContainers.remove(j);
                    instructionIndex[removed.ordinal()] = -1;
                }

                // add new containers to the stack
                for (int j = diffIdx; j < container.size(); j++) {
                    InstructionContainer added = container.get(j);
                    instructionIndex[added.ordinal()]++;
                    prevInstructionsContainers.add(added);
                }

                // The parent container is executing the next instruction too => Needs to be increased
                // Or the parent container finished executing a sub-procedure => Needs to be increased
                // Or the current container just goes to the next instruction => Needs to be increased
                if (diffIdx > 0) {
                    instructionIndex[prevInstructionsContainers.get(diffIdx - 1).ordinal()]++;
                }

                // reset the procedure instruction containers if all of their instructions are done
                for (int l = prevInstructionsContainers.size() - 1; l > 0; l--) {
                    int insIdx = prevInstructionsContainers.get(l).ordinal();
                    if (instructionIndex[insIdx] >= lengths[insIdx]) {
                        instructionIndex[insIdx] -= lengths[insIdx];
                        // also increment the instruction container where the procedure was called from
                        instructionIndex[prevInstructionsContainers.get(l - 1).ordinal()]++;
                    }
                }

                // add a mark transition of the specified instruction to the animation
                animations.getChildren().add(createMarkInstructionTransition(
                        Arrays.copyOf(instructionIndex, instructionIndex.length)
                ));
            }

            // create the animation of the current instruction with a preceding delay
            double moveLength = gameGrid.getWidth() / gameGrid.getColumnCount();
            animations.getChildren().add(createAnimationFromAction(currentMove, player, moveLength, speed.getSpeed()));
            animations.getChildren().add(createAnimationDelay(speed.getSpeed()));
        }

        this.animation = animations;

        if (animations.getChildren().isEmpty()) {
            showGameResult(movesCopy.get(movesCopy.size() - 1).getAction());
        } else {
            // remove the player image from the grid
            gridPlayer.setImage(AVAILABLE_FIELD_IMAGES.get(FieldType.NORMAL.ordinal()));

            // add the new player image to the grid
            gameGrid.add(player, playerCoord.getCol(), playerCoord.getRow());
            player.toFront();
            animations.play();
            animations.onFinishedProperty().set((ActionEvent actionEvent) -> {
                // remove the player image used for the animation
                removeAnimationPlayer();

                // make the user unable to press the stop button after the animation finished
                this.startBtn.setDisable(true);

                // set the player to his right ending location
                Coord lastBotPosition = conversionResult.getLastMove().getCurrentBotPosition();
                BotRotation lastBotRotation = conversionResult.getLastMove().getCurrentBotRotation();
                ImageView newGridPlayer = gameFields[lastBotPosition.getRow()][lastBotPosition.getCol()];
                newGridPlayer.setImage(AVAILABLE_FIELD_IMAGES.get(FieldType.START.ordinal()));
                newGridPlayer.setRotate(lastBotRotation.getRotation());
                showGameResult(movesCopy.get(movesCopy.size() - 1).getAction());
            });
        }
    }

    /**
     * Creates the PauseTransition that is used to mark the currently running instructions.
     *
     * @param instructionIndex index of the currently running instruction in the instruction container
     * @return blue frame animation (PauseTransition)
     */
    private PauseTransition createMarkInstructionTransition(int[] instructionIndex) {
        PauseTransition markTransition = new PauseTransition(Duration.ONE);
        // marks the current program or procedure instruction if the specific instruction container is on the stack of the current instruction
        markTransition.setOnFinished(ignore -> {
            currentInstructionProgramImage = markSingleInstruction(
                    instructionIndex[InstructionContainer.PROGRAM.ordinal()],
                    programFields,
                    currentInstructionProgramImage
            );
            currentInstructionP1Image = markSingleInstruction(
                    instructionIndex[InstructionContainer.PROCEDURE_ONE.ordinal()],
                    p1Fields,
                    currentInstructionP1Image
            );
            currentInstructionP2Image = markSingleInstruction(
                    instructionIndex[InstructionContainer.PROCEDURE_TWO.ordinal()],
                    p2Fields,
                    currentInstructionP2Image
            );
        });
        return markTransition;
    }

    /**
     * Stops the level animation and gives a game result when the animation finished
     */
    public void stopAnimation() {
        this.startBtn.setDisable(true);
        if (this.animation != null) {
            this.animation.pause();
            message.openMessageDialog(message.getLanguageBundleString("msg.animation-stopped"), MessageType.INFORMATION);
        }
    }

    /**
     * removes the player image, used for the animation, from the game field
     */
    public void removeAnimationPlayer() {
        gameGrid.getChildren().remove(this.player);
    }

    @Override
    public void showErrorMessage(Exception e) {
        this.borderPane.getCenter().setDisable(true);
        this.borderPane.getRight().setDisable(true);
        this.levelMenu.setDisable(true);

        if (e instanceof JsonParseException) {
            message.openErrorMessageDialog(
                    message.getLanguageBundleString("msg.error.cant-parse-json"),
                    e.getMessage());
        } else if (e instanceof EmptyFileException) {
            message.openMessageDialog(
                    message.getLanguageBundleString("msg.error.empty-file"),
                    MessageType.ERROR
            );
        } else if (e instanceof InvalidBotRotationException) {
            message.openErrorMessageDialog(
                    message.getLanguageBundleString("msg.error.invalid-bot-rotation"),
                    e.getMessage()
            );
        } else if (e instanceof InvalidFieldTypeException) {
            message.openErrorMessageDialog(
                    message.getLanguageBundleString("msg.error.invalid-field-type"),
                    e.getMessage()
            );
        } else if (e instanceof IOException) {
            message.openErrorMessageDialog(
                    message.getLanguageBundleString("msg.error.io-exception"),
                    e.getMessage()
            );
        } else if (e instanceof InvalidFieldSizeException) {
            message.openErrorMessageDialog(
                    message.getLanguageBundleString("msg.error.invalid-field-size"),
                    e.getMessage()
            );
        } else if (e instanceof NoFieldException) {
            message.openErrorMessageDialog(
                    message.getLanguageBundleString("msg.error.no-field"),
                    e.getMessage()
            );
        } else if (e instanceof NoBotRotationException) {
            message.openErrorMessageDialog(
                    message.getLanguageBundleString("msg.error.no-bot-rotation"),
                    e.getMessage()
            );
        }
    }

    @Override
    public void showLevelSolverMessage(SolveStatus solvable) {
        MessageType messageType = MessageType.WARNING;
        if (solvable == SolveStatus.SOLVABLE) {
            messageType = MessageType.INFORMATION;
        }

        String messageProperty = "";
        switch (solvable) {
            case SOLVABLE:
                messageProperty = "msg.solver.solvable";
                break;
            case UNSOLVABLE_INVALID_LEVEL:
                messageProperty = "msg.solver.unsolvable-invalid-level";
                break;
            case UNSOLVABLE_CAN_NOT_REACH_ALL_COINS:
                messageProperty = "msg.solver.unsolvable-can-not-reach-all-coins";
                break;
            case UNSOLVABLE_CAN_NOT_REACH_DOOR:
                messageProperty = "msg.solver.unsolvable-can-not-reach-door";
                break;
            case UNSOLVABLE_CAN_NOT_REDUCE_TO_PROGRAM_AND_PROCEDURES:
                messageProperty = "msg.solver.unsolvable-can-not-reduce-to-program-and-procedures";
                break;
        }

        message.openMessageDialog(
                message.getLanguageBundleString(messageProperty),
                messageType
        );
    }

    @Override
    public void useProcedureOne(ProcedureOne p1) {
        this.p1Control.setUserData(p1);
    }

    @Override
    public void useProcedureTwo(ProcedureTwo p2) {
        this.p2Control.setUserData(p2);
    }

    @Override
    public void addProgramInstruction(Instruction selectedInstruction, Coord coord) {
        setInstructionImage(selectedInstruction, this.programFields, coord);
    }

    @Override
    public void fillProgramInstructions(List<Instruction> instructions) {
        deleteAllProgramInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            setInstructionImage(instructions.get(i), programFields, Coord.convertIndexToCoord(i, INSTRUCTION_COLS));
        }
    }

    @Override
    public void addP1Instruction(Instruction selectedInstruction, Coord coord) {
        setInstructionImage(selectedInstruction, this.p1Fields, coord);
    }

    @Override
    public void fillP1Instructions(List<Instruction> instructions) {
        deleteAllP1Instructions();
        for (int i = 0; i < instructions.size(); i++) {
            setInstructionImage(instructions.get(i), this.p1Fields, Coord.convertIndexToCoord(i, INSTRUCTION_COLS));
        }
    }

    @Override
    public void addP2Instruction(Instruction selectedInstruction, Coord coord) {
        setInstructionImage(selectedInstruction, this.p2Fields, coord);
    }

    @Override
    public void fillP2Instructions(List<Instruction> instructions) {
        deleteAllP2Instructions();
        for (int i = 0; i < instructions.size(); i++) {
            setInstructionImage(instructions.get(i), this.p2Fields, Coord.convertIndexToCoord(i, INSTRUCTION_COLS));
        }
    }

    @Override
    public void deleteProgramInstruction(Coord deleteCoord, Coord lastCoord) {
        promoteInstructions(this.programFields, deleteCoord, lastCoord);
    }

    @Override
    public void deleteP1Instruction(Coord deleteCoord, Coord lastCoord) {
        promoteInstructions(this.p1Fields, deleteCoord, lastCoord);
    }

    @Override
    public void deleteP2Instruction(Coord deleteCoord, Coord lastCoord) {
        promoteInstructions(this.p2Fields, deleteCoord, lastCoord);
    }

    /**
     * Sets the image of a given grid position to the image of the specified instruction.
     * The grid can either be the program grid or one of the procedure grids.
     *
     * @param instruction instruction to change the image to
     * @param container   holds all the images of a given instruction grid (program, procedure 1, procedure 2)
     * @param coord       coordinate where the new instruction image should be displayed
     */
    private void setInstructionImage(Instruction instruction, ImageView[][] container, Coord coord) {
        ImageView requestedCell = container[coord.getRow()][coord.getCol()];
        requestedCell.setImage(AVAILABLE_INSTRUCTION_IMAGES.get(instruction.toString()));
    }

    /**
     * This method is used to fill the gap that appears when an instruction is being removed from an instruction grid.
     * To fill the gap, every instruction after the deleted one will move one cell to the left, closing the gap.
     *
     * @param container   instruction grid (program, procedure 1, procedure 2)
     * @param deleteCoord coordinate of the instruction which should be deleted
     * @param lastCoord   coordinate of the last instruction that was added to that container
     */
    private void promoteInstructions(ImageView[][] container, Coord deleteCoord, Coord lastCoord) {
        Image lastImage = AVAILABLE_FIELD_IMAGES.get(FieldType.NORMAL.ordinal());
        int deleteIdx = Coord.convertCoordToIndex(deleteCoord, INSTRUCTION_COLS);
        int lastIdx = Coord.convertCoordToIndex(lastCoord, INSTRUCTION_COLS);

        for (int i = lastIdx; i >= deleteIdx; i--) {
            Coord current = Coord.convertIndexToCoord(i, INSTRUCTION_COLS);
            Image temp = container[current.getRow()][current.getCol()].getImage();
            container[current.getRow()][current.getCol()].setImage(lastImage);
            lastImage = temp;
        }
    }

    @Override
    public void deleteAllProgramInstructions() {
        deleteAllInstructions(programFields);
    }

    @Override
    public void deleteAllP1Instructions() {
        deleteAllInstructions(p1Fields);
    }

    @Override
    public void deleteAllP2Instructions() {
        deleteAllInstructions(p2Fields);
    }

    public void removeInstructionMarks() {
        currentInstructionProgramImage.setEffect(null);
        currentInstructionP1Image.setEffect(null);
        currentInstructionP2Image.setEffect(null);
    }

    /**
     * Goes through every image of a given instruction container and sets the images back to the default image.
     *
     * @param container instruction container images (program, procedure 1, procedure 2)
     */
    private void deleteAllInstructions(ImageView[][] container) {
        for (ImageView[] row : container) {
            for (ImageView cell : row) {
                cell.setImage(AVAILABLE_FIELD_IMAGES.get(3));
            }
        }
    }

    /**
     * Changes the game speed to the given speed.
     *
     * @param speed new game speed
     */
    public void changeSpeed(GameSpeed speed) {
        this.speed = speed;
    }
}
