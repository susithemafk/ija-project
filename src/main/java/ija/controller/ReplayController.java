package ija.controller;

import ija.model.GameBoard;
import ija.replay.ReplayManager;
import ija.util.Constants;
import ija.view.ReplayWindow;
import javafx.stage.Window;

public class ReplayController {
    private ReplayManager replayManager;
    private ReplayWindow replayWindow;
    private Window ownerWindow;
    private GameController gameController;

    public ReplayController(Window owner, GameController gameController) {
        this.ownerWindow = owner;
        this.gameController = gameController;
        this.replayManager = new ReplayManager(Constants.LOG_FILE_NAME);
    }

    public void startReplay() {
        // načtení aktuálního logu
        if (replayManager.loadLog()) { // Načte aktuální game_log.log

            // vytvoření nového okna pro replay
            if (replayWindow == null || !replayWindow.isShowing()) {
                replayWindow = new ReplayWindow(ownerWindow, replayManager.getBoardWidth(),
                        replayManager.getBoardHeight());

                // nastavení akčních tlačítek
                setupButtonActions();

                // zobrazení okna
                replayWindow.show();
            } else {
                // popř. přenesení otevřeného replay okna do popředí
                replayWindow.show();
            }

            // nastavení počátečního stavu
            updateReplayView();
        } else {
            gameController.getStatusView().showMessage("Nelze nalézt log soubor programu.", true);
            System.err.println("Chyba při načítání aktuálního logu.");
        }
    }

    /**
     * Nastaví akce pro tlačítka např. tlačítka pro krokování.
     */
    private void setupButtonActions() {
        replayWindow.getButtonPrevious().setOnAction(e -> {
            if (replayManager.previousStep()) {
                updateReplayView();
            }
        });
        replayWindow.getButtonNext().setOnAction(e -> {
            if (replayManager.nextStep()) {
                updateReplayView();
            }
        });
        replayWindow.getBtnContinuePlaying().setOnAction(e -> {
            continuePlayingFromThisState();
        });
    }

    /**
     * Aktualizuje zobrazení replaye podle aktuálního stavu hry.
     */
    private void updateReplayView() {
        GameBoard currentBoard = replayManager.getCurrentBoardState();
        if (currentBoard != null && replayWindow != null && replayWindow.isShowing()) {
            replayWindow.getReplayBoardView().drawBoard(currentBoard);
            replayWindow.getReplayBoardView().updateAllTiles();
        }

        // disable tlačítek pokud už nejde pokračovat
        if (replayWindow != null) {
            replayWindow.getButtonPrevious().setDisable(!replayManager.hasPreviousStep());
            replayWindow.getButtonNext().setDisable(!replayManager.hasNextStep());
        }
    }

    /**
     * Přepíše aktuální log aktuálním stavem replaye a zavře okno replaye.
     */
    private void continuePlayingFromThisState() {
        // přepsání aktuálního logu aktuálním stavem replaye
        boolean success = replayManager.writeCurrentReplayStateToLog(Constants.LOG_FILE_NAME);

        if (success) {
            // přepsání gamecontrolleru z aktuálního logu
            gameController.takeOverGameFromCurrentLog();

            // zavření okna
            if (replayWindow != null && replayWindow.isShowing()) {
                replayWindow.close();
            }

        } else {
            System.err.println("Chyba při přepisování aktuálního logu z replaye.");
            gameController.getStatusView().showMessage("Chyba při návratu z replaye.", true);
        }
    }

    /**
     * Zavře okno replaye, pokud je otevřené.
     */
    public void closeWindow() {
        if (replayWindow != null && replayWindow.isShowing()) {
            replayWindow.close();
        }
    }

    /**
     * Zjistí, zda je okno replaye otevřené.
     */
    public boolean isWindowShowing() {
        return replayWindow != null && replayWindow.isShowing();
    }
}