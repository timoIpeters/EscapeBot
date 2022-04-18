package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
import logic.GameLogic;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Main Controller for the FXMLDocument specifying all the games UI events.
 *
 * @author Timo Peters
 */
public class FXMLDocumentController implements Initializable {

    /**
     * UI Wrapper surrounding all elements
     */
    @FXML
    private BorderPane borderPane;

    /**
     * gridPane representing the game grid
     */
    @FXML
    private GridPane gameGrid;

    /**
     * wraps the gameGrid
     */
    @FXML
    private VBox vBoxWrappingGrdPn;

    /**
     * wraps the VBox, fills the center of the borderpane
     */
    @FXML
    private HBox hBoxWrappingVBox;

    /**
     * Container for the program and procedure UI elements
     */
    @FXML
    private VBox programContainer;

    /**
     * Container for the control UI elements (instructions/field-types and toggle, solve and start button)
     */
    @FXML
    private VBox controlsContainer;

    /**
     * Grid representing the main program instruction UI elements
     */
    @FXML
    private GridPane mpGrid;

    /**
     * Grid representing the UI elements of the first procedure
     */
    @FXML
    private GridPane p1Grid;

    /**
     * Grid representing the UI elements of the second procedure
     */
    @FXML
    private GridPane p2Grid;

    /**
     * Right side of the BorderPane containing programContainer and controlsContainer
     */
    @FXML
    private HBox rightHalf;

    /**
     * Menu inside the menu bar representing the changeable game speed
     */
    @FXML
    private Menu speedMenu;

    /**
     * ToggleGroup of the speed menu inside the menu bar
     */
    @FXML
    private ToggleGroup speed;

    /**
     * Button to switch between Editor Mode and Play Mode
     */
    @FXML
    private Button toggleModeBtn;

    /**
     * Button to solve the level in Editor Mode
     */
    @FXML
    private Button solveBtn;

    /**
     * Button to start the program execution
     */
    @FXML
    private Button startBtn;

    /**
     * Button to delete the current instructions inside the program gird
     */
    @FXML
    private Button deleteProgramBtn;

    /**
     * Button to delete the current instructions inside the first procedure grid
     */
    @FXML
    private Button deleteP1Btn;

    /**
     * Button to delete the current instructions inside the second procedure grid
     */
    @FXML
    private Button deleteP2Btn;

    /**
     * MenuItem to create a new level
     */
    @FXML
    private MenuItem newLevelMenuItem;

    /**
     * Menu at the top of the screen
     */
    @FXML
    private Menu levelMenu;

    /**
     * List of multiple nodes on the right program half that should be disabled when running the game
     * This list includes the toggle mode button and the complete program container (including program and procedure
     * containers with their delete buttons)
     */
    @FXML
    private ArrayList<Node> rightHalfDisableGroup;

    /**
     * ResourceBundle containing all the UI string properties
     */
    private ResourceBundle languageBundle;

    /**
     * Message object to create different message dialogs
     */
    private Message message;

    /**
     * Boolean value of the current mode (play = true, editor = false)
     */
    private boolean isInPlayMode;

    /**
     * Boolean value indicating if the game is currently running (running = true, not running = false)
     */
    private Boolean isRunning = false;

    /**
     * Reference to the game logic
     */
    private GameLogic game;

    /**
     * Reference to the GUI
     */
    private JavaFXGUI gui;

    /**
     * Static UI field containing the instruction controls
     */
    private VBox INSTRUCTION_CONTROLS;

    /**
     * Static UI field containing the field controls
     */
    private VBox FIELD_CONTROLS;

    /**
     * Image of the currently selected control
     */
    private static ImageView currentControlImage;

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
     * Placeholder at the bottom of the border pane which is being replaced with the actual message whenever a method of
     * the Message instance is called
     */
    @FXML
    private Pane messagePlaceholder;

    /**
     * Creates an ImageView for every possible field control.
     * The Images are later displayed in the controls section of the UI.
     */
    private void configureFieldImages() {
        for (FieldType val : FieldType.values()) {
            ImageView imageView = createImageView(val);
            FIELD_CONTROLS.getChildren().add(imageView);
        }
    }

    /**
     * Creates an ImageView for every possible instruction.
     * The Images are later displayed in the controls section of the UI.
     */
    private void configureControlImages() {
        for (Instruction ins : AVAILABLE_INSTRUCTIONS) {
            ImageView imageView = createImageView(ins);
            INSTRUCTION_CONTROLS.getChildren().add(imageView);
        }
    }

    /**
     * Utility function to create an ImageView from a given objects.
     * The name of the image comes form the objects string representation.
     *
     * @param val Object which will be passed into the user data of the ImageView.
     * @return ImageView with the given object as user data
     */
    private ImageView createImageView(Object val) {
        Image image = new Image(Objects.requireNonNull(FXMLDocumentController.class.getResourceAsStream("/gui/img/" + val.toString() + ".png")));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        imageView.setImage(image);
        imageView.setUserData(val);
        return imageView;
    }

    /**
     * Marks the currently selected control with a red border.
     *
     * @param control Node to mark (only works with ImageViews)
     */
    private static void markControl(Node control) {
        if (control instanceof ImageView) {
            if (currentControlImage != null) {
                currentControlImage.setEffect(null);
            }
            currentControlImage = (ImageView) control;
            currentControlImage.setEffect(new InnerShadow(20.0, Color.RED));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize the basic UI grids (game and instruction containers)
        ImageView[][] gameFields = fillGrid(this.gameGrid);
        ImageView[][] programFields = fillGrid(this.mpGrid);
        ImageView[][] procedureOneFields = fillGrid(this.p1Grid);
        ImageView[][] procedureTwoFields = fillGrid(this.p2Grid);

        this.isInPlayMode = true;
        this.languageBundle = resourceBundle;

        // initialization of the message object
        // also contains a callback function that specifies what should be done on the UI if the user presses the
        // message button
        this.message = new Message(borderPane, languageBundle, ignore -> {
            // reset the game and clean up the animation
            this.game.reset();
            this.gui.removeInstructionMarks();
            this.gui.removeAnimationPlayer();

            // remove the message from the borderPane
            this.borderPane.setBottom(messagePlaceholder);

            // re-enable all the nodes that were disabled while the animation was running
            this.borderPane.getCenter().setDisable(false);
            this.borderPane.getRight().setDisable(false);
            levelMenu.setDisable(false);
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.setResizable(true);

            // enable all UI components that should be enabled when starting the application
            this.rightHalfDisableGroup.forEach(node -> node.setDisable(!this.isInPlayMode));
            this.toggleModeBtn.setDisable(false);
            this.controlsContainer.setDisable(false);
            this.startBtn.setDisable(!this.isInPlayMode);
            this.solveBtn.setDisable(this.isInPlayMode);

            // switch the stop button to be a start button again
            this.startBtn.setText(languageBundle.getString("btn.start.txt"));
            this.isRunning = false;
        });

        // Initialization of the instruction container
        INSTRUCTION_CONTROLS = new VBox();
        INSTRUCTION_CONTROLS.setAlignment(Pos.CENTER);
        INSTRUCTION_CONTROLS.setCursor(Cursor.HAND);
        INSTRUCTION_CONTROLS.setOnMouseClicked(event -> markControl(event.getPickResult().getIntersectedNode()));
        configureControlImages();
        this.rightHalfDisableGroup.add(INSTRUCTION_CONTROLS);

        // Initialization of the field container
        FIELD_CONTROLS = new VBox();
        FIELD_CONTROLS.setAlignment(Pos.CENTER);
        FIELD_CONTROLS.setCursor(Cursor.HAND);
        FIELD_CONTROLS.setOnMouseClicked(event -> markControl(event.getPickResult().getIntersectedNode()));
        configureFieldImages();

        //grdPn width and height both are bound to width of wrapping box
        gameGrid.prefWidthProperty().bind(vBoxWrappingGrdPn.widthProperty());
        gameGrid.prefHeightProperty().bind(vBoxWrappingGrdPn.widthProperty());

        // width of all the UI elements on the right window half (everything except the game grid)
        rightHalf.prefWidthProperty().bind(borderPane.widthProperty().divide(3));

        //grid squared, but possible gap between grid and controls
        vBoxWrappingGrdPn.prefWidthProperty().bind(hBoxWrappingVBox.heightProperty());

        // Initially show the instruction controls
        controlsContainer.getChildren().add(INSTRUCTION_CONTROLS);

        // Mark the first instruction control when the game loads
        currentControlImage = (ImageView) INSTRUCTION_CONTROLS.getChildren().get(0);
        markControl(currentControlImage);

        // gameGrid NOT clickable
        gameGrid.setDisable(true);
        gameGrid.setCursor(Cursor.DEFAULT);

        // a new level can only be created while being in the editor
        newLevelMenuItem.setDisable(true);

        // program and procedure fields clickable
        mpGrid.setCursor(Cursor.HAND);
        p1Grid.setCursor(Cursor.HAND);
        p2Grid.setCursor(Cursor.HAND);

        // show indicator for clickable buttons
        solveBtn.setDisable(true);
        solveBtn.setCursor(Cursor.HAND);
        toggleModeBtn.setCursor(Cursor.HAND);
        startBtn.setCursor(Cursor.HAND);
        deleteProgramBtn.setCursor(Cursor.HAND);
        deleteP1Btn.setCursor(Cursor.HAND);
        deleteP2Btn.setCursor(Cursor.HAND);

        // set the user data for every speed option
        for (int i = 0; i < speed.getToggles().size(); i++) {
            speed.getToggles().get(i).setUserData(GameSpeed.values()[i]);
        }
        GameSpeed initialGameSpeed = (GameSpeed) speed.getSelectedToggle().getUserData();

        // get the p1 and p2 image
        ImageView p1 = (ImageView) INSTRUCTION_CONTROLS.getChildren().get(INSTRUCTION_CONTROLS.getChildren().size() - 2);
        ImageView p2 = (ImageView) INSTRUCTION_CONTROLS.getChildren().get(INSTRUCTION_CONTROLS.getChildren().size() - 1);

        // initialize GUI and game
        this.gui = new JavaFXGUI(gameFields, programFields,
                procedureOneFields, procedureTwoFields, p1, p2, message, this.borderPane, this.levelMenu,
                this.startBtn, this.rightHalfDisableGroup, this.gameGrid, initialGameSpeed);
        this.game = new GameLogic(this.gui);
    }

    /**
     * Handles clicks on the game grid while in Editor Mode.
     * Left-click changes the clicked game grid cell to the currently selected field type.
     * Right-click on the bot changes its rotation clock-wise.
     *
     * @param mouseEvent Information about the pressed mouse button and the current mouse position
     */
    @FXML
    private void onGameGridClicked(MouseEvent mouseEvent) {
        // initialize x and y with default values
        Coord coord = getClickedGridCoord(mouseEvent, gameGrid);
        if (coord == null) {
            return;
        }

        // check which mouse button the player pressed
        boolean leftClicked = mouseEvent.getButton() == MouseButton.PRIMARY;
        boolean rightClicked = mouseEvent.getButton() == MouseButton.SECONDARY;

        FieldType selectedField = (FieldType) currentControlImage.getUserData();
        if (leftClicked && selectedField != null) {
            // reset the image of the old door/player positions
            if (selectedField == FieldType.DOOR || selectedField == FieldType.START) {
                game.resetAllOccurrencesOfField(selectedField);
            }
            game.changeFieldType(coord, selectedField);
        } else if (rightClicked && selectedField == FieldType.START && coord.equals(this.game.getBotPosition())) {
            game.rotateBot();
        }
    }

    /**
     * Called when clicking on a cell in the program instruction grid. This method just calls the {@link #onInstructionGridClicked(MouseEvent, BiConsumer, Consumer)}
     * method with a BiConsumer to add program instructions and a Consumer to delete program instructions.
     *
     * @param mouseEvent Information about the pressed mouse button and the current mouse position
     */
    @FXML
    private void onProgramInstructionGridClicked(MouseEvent mouseEvent) {
        this.onInstructionGridClicked(mouseEvent, game::addProgramInstruction, game::deleteProgramInstruction);
    }

    /**
     * Called when clicking on a cell in the procedureOne instruction grid. This method just calls the {@link #onInstructionGridClicked(MouseEvent, BiConsumer, Consumer)}
     * method with a BiConsumer to add procedureOne instructions and a Consumer to delete procedureOne instructions.
     *
     * @param mouseEvent Information about the pressed mouse button and the current mouse position
     */
    @FXML
    private void onP1InstructionGridClicked(MouseEvent mouseEvent) {
        this.onInstructionGridClicked(mouseEvent, game::addP1Instruction, game::deleteP1Instruction);
    }

    /**
     * Called when clicking on a cell in the procedureTwo instruction grid. This method just calls the {@link #onInstructionGridClicked(MouseEvent, BiConsumer, Consumer)}
     * method with a BiConsumer to add procedureTwo instructions and a Consumer to delete procedureTwo instructions.
     *
     * @param mouseEvent Information about the pressed mouse button and the current mouse position
     */
    @FXML
    private void onP2InstructionGridClicked(MouseEvent mouseEvent) {
        this.onInstructionGridClicked(mouseEvent, game::addP2Instruction, game::deleteP2Instruction);
    }

    /**
     * Handles clicks on one of the three instruction grids (program, procedure 1, procedure 2).
     * The functions that are executed on a left-click or a right-click are passed as parameters (Consumers).
     * <p>
     * The onLeftClick argument is a BiConsumer which represents an operation that accepts two input arguments and
     * returns void. It is used to call the correct addInstruction method depending on which instruction grid is clicked.
     * <p>
     * The onRightClick argument is a Consumer which represents an operation that accepts one input argument and
     * returns void. It is used to call the correct deleteInstruction method depending on which instruction grid is clicked.
     * <p>
     * When calling the .accept() method on the Consumer or BiConsumer, the callback-method, given as parameter, can be called
     * with arguments specified as parameters to the .accept() function depending on what Consumer it is called on.
     *
     * @param mouseEvent   Information about the pressed mouse button and the current mouse position
     * @param onLeftClick  Function with 2 parameters which will be executed when left-clicking on an instruction
     * @param onRightClick Function with 1 parameter which will be executed when right-clicking on an instruction
     */
    private void onInstructionGridClicked(MouseEvent mouseEvent, BiConsumer<Instruction, Coord> onLeftClick, Consumer<Coord> onRightClick) {
        // check which mouse button the player pressed
        boolean leftClicked = mouseEvent.getButton() == MouseButton.PRIMARY;
        boolean rightClicked = mouseEvent.getButton() == MouseButton.SECONDARY;

        // get the coordinate in the instruction grid, the user has pressed
        Coord coord = getClickedGridCoord(mouseEvent, (GridPane) mouseEvent.getSource());
        if (coord == null) return;

        Instruction selectedInstruction = (Instruction) currentControlImage.getUserData();
        if (leftClicked) {
            // add instruction to program or procedure
            onLeftClick.accept(selectedInstruction, coord);
        } else if (rightClicked) {
            // remove instruction from program or procedure
            // the other instructions should catch up with the instruction before the deleted one
            onRightClick.accept(coord);
        }
    }

    /**
     * Gets the Coordinate where the user clicked on the grid.
     *
     * @param mouseEvent Information about the pressed mouse button and the current mouse position
     * @param grid       clickable grid
     * @return coordinate of the clicked grid cell
     */
    private Coord getClickedGridCoord(MouseEvent mouseEvent, GridPane grid) {
        // initialize x and y with default values
        Coord coord = new Coord(-1, -1);

        // get the coordinate at the given mouse position
        for (Node node : grid.getChildren()) {
            if (node instanceof ImageView) {
                if (node.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                    coord.setRow(GridPane.getRowIndex(node));
                    coord.setCol(GridPane.getColumnIndex(node));
                }
            }
        }

        // no coordinate found
        if (coord.getRow() == -1 || coord.getCol() == -1) {
            return null;
        }

        assert (coord.getRow() >= 0 && coord.getCol() >= 0) : "The click was not convertable into a coordinate";
        return coord;
    }

    /**
     * Configures the given GridPanes properties and fills the grid with default imageViews.
     *
     * @param grdPn grid to initialize
     * @return 2-dimensional array of ImageViews representing the grid content
     */
    public ImageView[][] fillGrid(GridPane grdPn) {
        // initialize game field
        int colCount = grdPn.getColumnCount();
        int rowCount = grdPn.getRowCount();
        ImageView[][] imageViews = new ImageView[rowCount][colCount];

        // go through every image of the grid
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                imageViews[row][col] = new ImageView();

                // images can be stretched
                imageViews[row][col].setPreserveRatio(false);

                //sets better quality filtering algorithm
                imageViews[row][col].setSmooth(true);

                //set start images for the program and procedures
                if (grdPn != this.gameGrid) {
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/img/NORMAL.png")));
                    imageViews[row][col].setImage(image);
                }

                // add the imageView to the cell
                grdPn.add(imageViews[row][col], col, row);

                // the image shall resize when the cell resizes
                imageViews[row][col].fitWidthProperty().bind(grdPn.widthProperty().divide(colCount));
                imageViews[row][col].fitHeightProperty().bind(grdPn.heightProperty().divide(rowCount));
            }
        }
        return imageViews;
    }

    /**
     * Switches the UI between Editor Mode and Play Mode.
     */
    @FXML
    private void switchMode() {
        ImageView firstControl;
        if (this.isInPlayMode) {
            switchToEditorMode();
            firstControl = (ImageView) FIELD_CONTROLS.getChildren().get(0);
        } else {
            switchToPlayMode();
            firstControl = (ImageView) INSTRUCTION_CONTROLS.getChildren().get(0);
        }
        this.isInPlayMode = !isInPlayMode;
        // when switching modes, the first control will be market in the controls' container in the middle of
        // the application
        markControl(firstControl);
    }

    /**
     * Switches to Editor Mode
     */
    private void switchToEditorMode() {
        toggleModeBtn.setText(languageBundle.getString("btn.playMode.txt"));

        // gameGrid clickable
        gameGrid.setDisable(false);
        gameGrid.setCursor(Cursor.HAND);

        // Enabled UI elements
        solveBtn.setDisable(false);
        newLevelMenuItem.setDisable(false);

        // Disabled UI elements
        programContainer.setDisable(true);
        speedMenu.setDisable(true);
        startBtn.setDisable(true);

        // Switch instructions with fields
        controlsContainer.getChildren().remove(INSTRUCTION_CONTROLS);
        controlsContainer.getChildren().add(FIELD_CONTROLS);
    }

    /**
     * Switches to Play Mode
     */
    private void switchToPlayMode() {
        toggleModeBtn.setText(languageBundle.getString("btn.editorMode.txt"));

        //gameGrid NOT clickable
        gameGrid.removeEventFilter(MouseEvent.MOUSE_CLICKED, this::onGameGridClicked);

        // gameGrid NOT clickable
        gameGrid.setDisable(true);
        gameGrid.setCursor(Cursor.DEFAULT);

        //Enable UI elements
        startBtn.setDisable(false);
        speedMenu.setDisable(false);
        programContainer.setDisable(false);
        newLevelMenuItem.setDisable(false);

        // make it visible for the user that he can click on the instruction grids
        mpGrid.setCursor(Cursor.HAND);
        p1Grid.setCursor(Cursor.HAND);
        p2Grid.setCursor(Cursor.HAND);

        //Disable UI elements
        solveBtn.setDisable(true);
        gameGrid.setDisable(true);
        newLevelMenuItem.setDisable(true);

        // Switch fields with instructions
        controlsContainer.getChildren().remove(FIELD_CONTROLS);
        controlsContainer.getChildren().add(INSTRUCTION_CONTROLS);

        // needed if the user added or removed coins while in editor mode
        game.updateCoins();
    }


    /**
     * Solves the level in Editor Mode, adding a possible solution to the program and procedures
     */
    @FXML
    private void solveLevel() {
        // disables UI components that should not be clickable after pressing the solveLevel button
        this.solveBtn.setDisable(true);
        this.toggleModeBtn.setDisable(true);
        this.borderPane.getCenter().setDisable(true);
        this.levelMenu.setDisable(true);
        this.controlsContainer.setDisable(true);

        game.solveLevel();
    }

    /**
     * Switches between the start and stop state of the game animation
     */
    @FXML
    private void switchStartStop() {
        if (this.isRunning) {
            stopLevel();
        } else {
            startLevel();
            this.isRunning = !this.isRunning;
        }
    }

    /**
     * Starts solving the current level with all the currently added instructions. Also changes the current start button
     * to be a stop button (due to the {@link #switchStartStop()} method it is a toggle button)
     */
    private void startLevel() {
        startBtn.setText(languageBundle.getString("btn.stop.txt"));
        if (game.validateLevel()) {
            game.startLevel();
        } else {
            message.openMessageDialog(languageBundle.getString("msg.error.invalid-game-field"), MessageType.ERROR);
        }
    }

    /**
     * Stops solving the current level. Also changes the current stop button
     * to be a start button (due to the {@link #switchStartStop()} method it is a toggle button)
     */
    private void stopLevel() {
        startBtn.setText(languageBundle.getString("btn.start.txt"));
        this.gui.stopAnimation();
    }

    /**
     * Removes all instructions from the program grid
     */
    @FXML
    private void deleteProgramInstructions() {
        game.deleteAllProgramInstructions();
    }

    /**
     * Removes all instructions from the procedure one grid
     */
    @FXML
    private void deleteP1Instructions() {
        game.deleteAllP1Instructions();
    }

    /**
     * Removes all instructions from the procedure two grid
     */
    @FXML
    private void deleteP2Instructions() {
        game.deleteAllP2Instructions();
    }

    /**
     * Clears out the game field by overriding the current UI field with default images and the game logic
     * representation with the field type FieldType.NORMAL.
     */
    @FXML
    private void newLevel() {
        if (!this.levelMenu.isDisable()) {
            game.newLevel();
        }
    }

    /**
     * Loads the given example level by overriding the current game field.
     * This action is only possible when the game is not running
     */
    @FXML
    private void loadExampleLevel(ActionEvent actionEvent) {
        MenuItem item = (MenuItem) actionEvent.getSource();

        // the example levels filename is declared in their user data in the fxml document
        Reader file = new InputStreamReader(Objects.requireNonNull(FXMLDocumentController.class.getResourceAsStream("exampleLevels/" + item.getUserData())));
        game.loadLevelFromReader(file);
    }

    /**
     * Starts the logic to load a level from a file using a FileChooser.
     */
    @FXML
    private void loadLevelFromFile() {
        if (!this.levelMenu.isDisable()) {
            FileChooser fileChooser = getFileChooser(languageBundle.getString("menu.level.load.txt"));
            File selectedFile = fileChooser.showOpenDialog(borderPane.getScene().getWindow());

            if (selectedFile != null) {
                game.loadLevelFromFile(selectedFile);
            }
        }
    }

    /**
     * Saves the current level to a file selected in a FileChooser.
     */
    @FXML
    private void saveLevelToFile() {
        if (!this.levelMenu.isDisable()) {
            FileChooser fileChooser = getFileChooser(languageBundle.getString("menu.level.save.txt"));
            File selectedFile = fileChooser.showSaveDialog(borderPane.getScene().getWindow());

            if (selectedFile != null) {
                game.saveLevelToFile(selectedFile);
            }
        }
    }

    /**
     * Creates and returns a file chooser with a given title.
     *
     * @param fileChooserTitle title of the opening file chooser window
     * @return created file chooser
     */
    private FileChooser getFileChooser(String fileChooserTitle) {
        // get the directory where the jar is located
        File currDir = null;
        try {
            currDir = new File(FXMLDocumentController.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        } catch (URISyntaxException ignore) {
            // We will just open the file chooser in some directory. This is just ux no essential function
        }

        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        if (currDir != null) {
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.setTitle(fileChooserTitle);

        // Add extension filters to the FileChooser
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON file (*.json)", "*.json");
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All files", "*");
        fileChooser.getExtensionFilters().addAll(jsonFilter, allFilter);

        return fileChooser;
    }

    /**
     * Changes the speed of the game animation.
     */
    @FXML
    private void changeSpeed() {
        RadioMenuItem selected = (RadioMenuItem) speed.getSelectedToggle();
        this.gui.changeSpeed((GameSpeed) selected.getUserData());
    }

    /**
     * Opens a custom help dialog which shows the controls & rules of the game or
     * the editor controls & rules depending on which mode is active.
     */
    @FXML
    private void showHelp() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        Parent root;

        // load the correct help dialog depending on the current mode
        try {
            if (this.isInPlayMode) {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLPlayHelpDocument.fxml")), languageBundle);
            } else {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLEditorHelpDocument.fxml")), languageBundle);
            }
        } catch (IOException e) {
            return;
        }

        // add content and OK button to the dialog
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> res = dialog.showAndWait();

        // close dialog when OK was pressed
        if (res.isPresent() && res.get() == ButtonType.OK) {
            dialog.close();
        }
    }

    /**
     * Closes the application. The user should not be encouraged to close the application during an animation. Therefore,
     * the menu item is also not clickable while the level menu group is disabled. However, the application can still be
     * closed using the keyboard shortcut (or the applications' close button from the specific operating system)
     * if necessary.
     */
    @FXML
    private void exitApplication() {
        if (this.isRunning) {
            this.gui.stopAnimation();
        }
        Platform.exit();
    }
}
