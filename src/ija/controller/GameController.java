/**
 * Prostředník mezi uživatelským rozhraním View a logikou hry.
 * Zde by se mělo volat setupNewGame(), saveGame(), atd.
 * Taky bysme zde měli řešit klik na dlaždici a říct GameManageru, aby otočil dlaždici.
 */
package ija.controller;

import ija.model.GameBoard;
import ija.model.GameManager;
import ija.persistence.SaveLoadManager;
import ija.replay.ReplayManager;
import ija.replay.ReplayManager.MoveRecord;
import ija.util.Constants;
import ija.view.BoardView;
import ija.view.GameWindow;
import ija.view.MenuView;
import ija.view.StatsView;
import ija.view.HelpView;
import ija.view.StatusView;
import ija.view.TileView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public class GameController {
    private GameManager gameManager; // instance herního manažera
    private BoardView boardView; // instance vizuální reprezentace hrací desky
    private MenuView menuView; // instance vizuální reprezentace menu
    private StatusView statusView; // instance vizuální reprezentace stavu hry
    private GameWindow gameWindow; // instance hlavního okna
    private HelpView helpView; // Reference na okno nápovědy
    private StatsView statsView; // instance okna pro statistiky
    private SaveLoadManager saveLoadManager; // instance pro ukládání a načítání her
    private ReplayManager replayManager; // instance replay manažera
    private ReplayController replayController; // instance pro replay

    public GameController(GameManager gameManager, GameWindow gameWindow) {
        this.gameManager = gameManager;
        this.gameWindow = gameWindow;
        this.boardView = gameWindow.getBoardView();
        this.menuView = gameWindow.getMenuView();
        this.statusView = gameWindow.getStatusView();
        this.saveLoadManager = new SaveLoadManager(); // Nebo použít konstantu
        this.replayManager = new ReplayManager(Constants.LOG_FILE_NAME);

    }

    // nastavení posluchačů událostí pro menu a hrací desku
    public void initialize() {
        // listener kliknutí na nová hra
        menuView.getNewGameItem().setOnAction(event -> startNewGame());

        // listener kliknutí na uložení hry
        menuView.getSaveGameItem().setOnAction(event -> handleSaveGame());

        // listener kliknutí na načtení hry
        menuView.getLoadGameItem().setOnAction(event -> loadGame());

        // listener kliknutí na nápovědu
        menuView.getShowHintItem().setOnAction(event -> toggleHelpView());

        // listener kliknutí na replay
        menuView.getReplayGameItem().setOnAction(event -> showReplay());

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
        if (helpView != null && helpView.isHelpOpen()) {
            helpView.closeHelp();
            helpView = null;
        }

        // zavření okna replay, pokud je otevřené
        if (replayController != null && replayController.isWindowShowing()) {
            replayController.closeWindow();
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
            if (helpView != null && helpView.isHelpOpen()) {
                helpView.updateHelp(gameManager.getBoard());
            }

            // výhra
            if (gameManager.checkWinCondition()) {
                statusView.showMessage("Výhra", false);

                // zákaz dalšího klikání na desku
                boardView.getGridPane().setOnMouseClicked(null);

                // zavření nápovědy
                if (helpView != null && helpView.isHelpOpen()) {
                    helpView.closeHelp();
                    helpView = null;
                }

                // zobrazení statistik
                Window ownerWindow = boardView.getGridPane().getScene().getWindow();
                replayManager = new ReplayManager(Constants.LOG_FILE_NAME);
                statsView = new StatsView(ownerWindow, gameManager.getBoard(), replayManager);
                statsView.showStats();
            }
        }
        // jinak klikl hráč vedle
    }

    // zobrazení nebo skrytí okna s nápovědou.
    private void toggleHelpView() {
        Window ownerWindow = boardView.getGridPane().getScene().getWindow();

        // vytvoříme nové okno nápovědy, pokud ještě neexistuje
        if (helpView == null || !helpView.isHelpOpen()) {
            helpView = new HelpView(ownerWindow, gameManager.getBoard());
            helpView.showHelpWindow();
        }
        // jinak ho zavřeme
        else {
            helpView.closeHelp();
        }
    }

    /**
     * Zobrazí replay okno.
     */
    private void showReplay() {
        Window ownerWindow = boardView.getGridPane().getScene().getWindow();
        replayController = new ReplayController(ownerWindow, this);
        replayController.startReplay();
    }

    /**
     * Převezme hru z aktuálního kroku replaye.
     * Smaže všechny následující tahy od aktuálního kroku.
     */
    public void takeOverGameState(GameBoard newBoard, int steps, List<MoveRecord> moveHistory) {
        gameManager.setBoard(newBoard);
        gameManager.setCountOfSteps(steps);

        boardView.drawBoard(newBoard);
        boardView.updateAllTiles();

        statusView.updateSteps(gameManager.getCountOfSteps());
        statusView.clearMessage();

        // vrácení handleru pro klikání na dlaždice v herním okně
        boardView.getGridPane().setOnMouseClicked(event -> handleBoardClick(event));
    }

    /**
     * Načte hru ze souboru a přepíše log aktuální hry.
     */
    private void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Načíst hru");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Uložené hry", "*.dat"));
        File file = fileChooser.showOpenDialog(gameWindow.getPrimaryStage());

        if (file != null) {
            // přepsání logu aktuální hry
            boolean success = saveLoadManager.prepareLogForLoadingGame(file.getAbsolutePath(), Constants.LOG_FILE_NAME);

            if (success) {
                // načtení hry z logu
                gameManager.loadGameFromLog();

                // aktualizace UI
                boardView.drawBoard(gameManager.getBoard());
                boardView.updateAllTiles();

                statusView.updateSteps(gameManager.getCountOfSteps());
                statusView.clearMessage();

                System.out.println("Hra načtena.");
            } else {
                statusView.showMessage("Chyba načítání uložené hry.", true);
            }
        }
    }

    /**
     * Uloží hru do souboru.
     */
    private void handleSaveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Uložit hru");
        fileChooser.setInitialFileName("savegame.dat");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Uložené hry", "*.dat"));
        File fileToSave = fileChooser.showSaveDialog(gameWindow.getPrimaryStage());

        if (fileToSave != null) {
            boolean success = saveLoadManager.saveGame(Constants.LOG_FILE_NAME, fileToSave.getAbsolutePath());

            if (success) {
                statusView.showMessage("Hra uložena.", false);
            } else {
                statusView.showMessage("Chyba při ukládání hry.", true);
            }
        }
    }

    /**
     * Přebírá hru z aktuálního game_log.log, který byl přepsán replayem.
     */
    public void takeOverGameFromCurrentLog() {
        gameManager.loadGameFromLog();

        // aktualizace UI
        boardView.drawBoard(gameManager.getBoard());
        boardView.updateAllTiles();
        statusView.updateSteps(gameManager.getCountOfSteps());
        statusView.clearMessage();

        // obnova klikání na herní desku
        boardView.getGridPane().setOnMouseClicked(event -> handleBoardClick(event));
    }

    /**
     * Vrátí instanci statusView pro zobrazení zpráv.
     */
    public StatusView getStatusView() {
        return statusView;
    }

}