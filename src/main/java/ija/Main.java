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
    /**
     * Inicializační metoda JavaFX. Vytváří GameManager, GameWindow a
     * GameController.
     *
     * @param stage hlavní okno aplikace
     */
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

    /**
     * Vstupní bod programu. Spouští JavaFX aplikaci.
     *
     * @param args argumenty příkazové řádky
     */
    public static void main(String[] args) {
        launch(args);
    }
}