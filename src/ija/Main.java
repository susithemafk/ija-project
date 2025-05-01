package ija;

import javafx.stage.Stage;
import ija.controller.GameController;
import ija.model.GameManager;
import ija.view.GameWindow;
import javafx.application.Application;

/**
 * Hlavní třída aplikace LightBulb Game.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // inicializace
            GameManager gameManager = new GameManager();
            GameWindow gameWindow = new GameWindow(stage);
            GameController gameController = new GameController(gameManager, gameWindow);
            gameController.initialize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}