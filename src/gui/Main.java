package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * JavaFX's entry point.
 * Contains the basic stage and scene setup and the used language bundle.
 *
 * @author Timo Peters
 */
public class Main extends Application {

    /**
     * Window minimum width
     */
    private static final int MIN_WIDTH = 1024;

    /**
     * Windows minimum height
     */
    private static final int MIN_HEIGHT = 720;

    /**
     * ResourceBundle of all the used language elements
     */
    private static final ResourceBundle LANGUAGE_BUNDLE = ResourceBundle.getBundle("gui.l18n.language", Locale.GERMAN);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLDocument.fxml")), LANGUAGE_BUNDLE);

        // set stage properties
        primaryStage.setTitle(LANGUAGE_BUNDLE.getString("window.title"));
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        // add application icon (window icon on the top left)
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/img/PlayerTransparent.png"))));

        primaryStage.setScene(new Scene(root, MIN_WIDTH, MIN_HEIGHT));
        primaryStage.show();

        // added to alert the user and close the application if an uncaught exception occurred
        Thread.currentThread().setUncaughtExceptionHandler((Thread th, Throwable ex) -> {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unerwarteter Fehler");
            alert.setContentText("Entschuldigung, das hätte nicht passieren dürfen!");
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}