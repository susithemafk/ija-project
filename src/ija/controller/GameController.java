/**
 * Prostředník mezi uživatelským rozhraním View a logikou hry. 
 * Zde by se mělo volat setupNewGame(), saveGame(), atd. 
 * Taky bysme zde měli řešit klik na dlaždici a říct GameManageru, aby otočil dlaždici. 
 */
package ija.controller;

import ija.model.GameManager;
import ija.view.BoardView;
import ija.view.GameWindow;
import ija.view.MenuView;
import ija.view.HelpView;
import ija.view.StatusView;
import ija.view.TileView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

public class GameController {
    private GameManager gameManager; // instance herního manažera
    private BoardView boardView; // instance vizuální reprezentace hrací desky
    private MenuView menuView; // instance vizuální reprezentace menu
    private StatusView statusView; // instance vizuální reprezentace stavu hry
    private GameWindow gameWindow; // instance hlavního okna
    private HelpView hintView = null; // Reference na okno nápovědy

    public GameController(GameManager gameManager, GameWindow gameWindow) {
        this.gameManager = gameManager;
        this.gameWindow = gameWindow;
        this.boardView = gameWindow.getBoardView();
        this.menuView = gameWindow.getMenuView();
        this.statusView = gameWindow.getStatusView();

    }

    // nastavení posluchačů událostí pro menu a hrací desku
    public void initialize() {
        // listener kliknutí na nová hra
        menuView.getNewGameItem().setOnAction(event -> startNewGame());

        // listener kliknutí na nápovědu
        menuView.getShowHintItem().setOnAction(event -> toggleHintView());

        // listener kliknutí na konec
        menuView.getExitItem().setOnAction(event -> Platform.exit());

        // spuštění nové hry při startu aplikace
        startNewGame();
    }

    // spuštění nové hry
    public void startNewGame() {
        System.out.println("GameController: Spouštění nové hry...");

        // restart herního manažera
        gameManager.setupNewGame();

        // přepsání desky
        boardView.drawBoard(gameManager.getBoard());

        // přepsání stavů napájení
        boardView.updateAllTiles();

        // přepsání počítadla kroků
        statusView.updateSteps(gameManager.getCountOfSteps());

        // přepsání zprávy
        statusView.clearMessage();

        // přidání listeneru na kliknutí na desku
        boardView.getGridPane().setOnMouseClicked(event -> handleBoardClick(event));

        // zavření okna nápovědy, pokud je otevřené
        if (hintView != null && hintView.isHelpOpen()) {
            hintView.closeHelp();
            hintView = null;
        }

        System.out.println("GameController: Nová hra připravena.");
    }

    // zpracování kliknutí na dlaždici
    private void handleBoardClick(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();

        // hledání TileView
        Node parent = clickedNode;
        while (parent != null && !(parent instanceof TileView)) {
            parent = parent.getParent();
        }

        // pokud bylo kliknuto na dlaždici
        if (parent instanceof TileView) {
            // přetypování parenta
            TileView clickedTileView = (TileView) parent;

            // souřadnice kliknuté dlaždice
            int colIndex = GridPane.getColumnIndex(clickedTileView);
            int rowIndex = GridPane.getRowIndex(clickedTileView);

            System.out.println("GameController: Klik na TileView na [" + rowIndex + "," + colIndex + "]");

            // otočení dlaždice
            gameManager.rotateTile(rowIndex, colIndex);

            // aktualizace vzhledu dlaždic
            boardView.updateAllTiles();

            // aktualizace počítadla kroků
            statusView.updateSteps(gameManager.getCountOfSteps());

            // aktualizace okna nápovědy
            if (hintView != null && hintView.isHelpOpen()) {
                hintView.updateHelp(gameManager.getBoard());
            }

            if (gameManager.checkWinCondition()) {
                statusView.showMessage("Výhra", false);

                // zákaz dalšího klikání na desku
                boardView.getGridPane().setOnMouseClicked(null);

                // Zavřít nápovědu při výhře?
                if (hintView != null && hintView.isHelpOpen()) {
                    hintView.closeHelp();
                    hintView = null;
                }
            }
        }
        // jinak klik vedle
    }

    // zobrazení nebo skrytí okna s nápovědou.
    private void toggleHintView() {
        Window ownerWindow = boardView.getGridPane().getScene().getWindow();

        // vytvoříme nové okno nápovědy, pokud ještě neexistuje
        if (hintView == null || !hintView.isHelpOpen()) {
            hintView = new HelpView(ownerWindow, gameManager.getBoard());
            hintView.showHelpWindow();
        }
        // jinak ho zavřeme
        else {
            hintView.closeHelp();
        }
    }

}