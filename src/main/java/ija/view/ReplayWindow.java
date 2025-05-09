package ija.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ReplayWindow {
    private Stage replayStage;
    private BoardView replayBoardView;
    private Button buttonPrevious, buttonNext;
    private Button btnContinuePlaying;

    public ReplayWindow(Window owner, int boardWidth, int boardHeight) {
        // nový stage
        replayStage = new Stage();
        replayStage.initOwner(owner);
        replayStage.initModality(Modality.NONE);
        replayStage.setTitle("Přehrávání hry");

        BorderPane rootLayout = new BorderPane();

        replayBoardView = new BoardView();
        replayBoardView.getGridPane().setPadding(new Insets(10));
        rootLayout.setCenter(replayBoardView.getGridPane());

        // akční tlačítka
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        buttonPrevious = new Button("Krok zpět");
        buttonNext = new Button("Další krok");

        // přidání tlačítek do HBoxu
        controls.getChildren().addAll(buttonPrevious, buttonNext);

        // nastavení tlačítek na spodek okna
        rootLayout.setBottom(controls);

        btnContinuePlaying = new Button("Pokračovat v hraní");
        controls.getChildren().add(btnContinuePlaying);

        Scene scene = new Scene(rootLayout, 700, 750);
        replayStage.setScene(scene);
    }

    public void show() {
        replayStage.show();
    }

    public void close() {
        replayStage.close();
    }

    public boolean isShowing() {
        return replayStage.isShowing();
    }

    public BoardView getReplayBoardView() {
        return replayBoardView;
    }

    public Button getButtonPrevious() {
        return buttonPrevious;
    }

    public Button getButtonNext() {
        return buttonNext;
    }

    public Button getBtnContinuePlaying() {
        return btnContinuePlaying;
    }

}