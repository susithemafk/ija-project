/**
 * Hlavní okno aplikace.
 */
package ija.view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GameWindow {
    private BoardView boardView; // vizuální reprezentace hrací desky
    private MenuView menuView; // vizuální reprezentace menu
    private StatusView statusView; // vizuální reprezentace stavu hry
    private BorderPane layout; // hlavní layout
    private Stage primaryStage; // primary stage pro save a load hry

    public GameWindow(Stage stage) {
        this.primaryStage = stage;

        // inicializace layoutu
        this.layout = new BorderPane();

        // vytvoření view pro menubar
        this.menuView = new MenuView();

        // vytvoření view pro hrací desku
        this.boardView = new BoardView();

        // vytvoření view pro status
        this.statusView = new StatusView();

        // sestavení layoutu
        layout.setTop(this.menuView.getMenuBar());
        layout.setCenter(this.boardView.getGridPane());
        layout.setBottom(this.statusView.getWrapper());

        // nastavení okna
        Scene scene = new Scene(layout, 700, 750);
        stage.setTitle("IJA Project - LightBulb Game");
        stage.setScene(scene);
        stage.show();
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public MenuView getMenuView() {
        return menuView;
    }

    public StatusView getStatusView() {
        return statusView;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}