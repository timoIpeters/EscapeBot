package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ResourceBundle;

/**
 * Class of utility functions to open different types of message dialogs.
 *
 * @author Timo Peters
 */
public class Message {

    /**
     * Parent UI element
     */
    private final BorderPane borderPane;

    /**
     * EventHandler function that is being executed whenever a message dialog is being closed by the user
     */
    private final EventHandler<ActionEvent> eventHandler;

    /**
     * ResourceBundle containing the UIs string properties
     */
    private final ResourceBundle languageBundle;

    /**
     * Constructor to create a Message instance.
     *
     * @param borderPane     Parent in which the message will be added
     * @param languageBundle language strings accessible using their property name
     * @param run            event that should be executed when closing a message dialog
     */
    public Message(BorderPane borderPane, ResourceBundle languageBundle, EventHandler<ActionEvent> run) {
        this.borderPane = borderPane;
        this.languageBundle = languageBundle;
        this.eventHandler = run;
    }

    /**
     * Creates a formatted message dialog with a message and messageType given as parameter.
     * Depending on the given message type there will be a different message background.
     *
     * @param message     Message to be displayed
     * @param messageType Type of message (e.g. INFORMATION, WARNING, ERROR)
     */
    public void openMessageDialog(String message, MessageType messageType) {
        GridPane messageGrid = createMessageGrid(messageType);

        // message container containing the given message
        HBox messageContainer = new HBox();
        messageContainer.getChildren().add(createMessageText(message, false));
        messageContainer.setAlignment(Pos.CENTER);

        // button to close the message
        Button button = createMessageButton();

        // Add the HBox and Button to the message grid
        messageGrid.add(messageContainer, 0, 0);
        messageGrid.add(button, 1, 0);

        this.borderPane.setBottom(messageGrid);
    }

    /**
     * Creates a formatted error message dialog with a message and a detailed errorDescription.
     *
     * @param message          message to display
     * @param errorDescription detailed description of where to find the error (typically from the thrown error itself)
     */
    public void openErrorMessageDialog(String message, String errorDescription) {
        GridPane messageGrid = createMessageGrid(MessageType.ERROR);

        // message container containing the given message
        VBox messages = new VBox();
        messages.getChildren().add(createMessageText(message, false));
        messages.getChildren().add(createMessageText(errorDescription, true));
        messages.setAlignment(Pos.CENTER);
        messages.setPrefWidth(Double.POSITIVE_INFINITY);

        // button to close the message
        Button button = createMessageButton();

        // Add the HBox and Button to the message grid
        messageGrid.add(messages, 0, 0);
        messageGrid.add(button, 1, 0);

        this.borderPane.setBottom(messageGrid);
    }

    /**
     * Utility function to create a uniform message grid.
     *
     * @param messageType type of message
     * @return GridPane representing the message container
     */
    private GridPane createMessageGrid(MessageType messageType) {
        GridPane messageGrid = new GridPane();
        // set background color by message type
        messageGrid.setStyle(getMessageBackgroundColor(messageType));

        // Set constraints
        ColumnConstraints CC1 = new ColumnConstraints();
        CC1.setPercentWidth(80.00);
        CC1.setFillWidth(true);

        ColumnConstraints CC2 = new ColumnConstraints();
        CC2.setPercentWidth(20.00);
        CC2.setFillWidth(true);

        RowConstraints RC = new RowConstraints();
        RC.setPrefHeight(this.borderPane.getHeight() / 8);
        RC.setFillHeight(true);

        messageGrid.getColumnConstraints().add(CC1);
        messageGrid.getColumnConstraints().add(CC2);
        messageGrid.getRowConstraints().add(RC);
        return messageGrid;
    }

    /**
     * Utility function which creates a message button to close the message dialog.
     *
     * @return button to close the message dialog
     */
    private Button createMessageButton() {
        Button button = new Button();
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setText(languageBundle.getString("message-dialog.btn.txt"));
        button.setOnAction(this.eventHandler);
        return button;
    }

    /**
     * Utility function which creates a message text from a given message.
     * There is also an option to make the message bold.
     *
     * @param message message to display
     * @param bold    option to display the message in a bold font-weight
     * @return Label containing the message text
     */
    private Label createMessageText(String message, boolean bold) {
        Label text = new Label(message);
        text.setFont(Font.font(16));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrapText(true);
        if (bold) {
            text.setStyle("-fx-font-weight: bold");
        }
        return text;
    }

    /**
     * Returns a JavaFX background-color style depending on the given message type
     *
     * @param messageType type of message
     * @return Representation of the JavaFX background-color style
     */
    private String getMessageBackgroundColor(MessageType messageType) {
        String colorStyle;
        switch (messageType) {
            case INFORMATION:
                colorStyle = "-fx-background-color: #238209;";
                break;
            case WARNING:
                colorStyle = "-fx-background-color: #FFCB30;";
                break;
            case ERROR:
                colorStyle = "-fx-background-color: #D12C2F;";
                break;
            default:
                colorStyle = "";
        }
        return colorStyle;
    }

    public String getLanguageBundleString(String propertyName) {
        return languageBundle.getString(propertyName);
    }
}
