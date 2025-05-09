/**
 * Vizuální reprezentace nápovědy v novém okně.
 */
package ija.view;

import ija.model.GameBoard;
import ija.model.Tile;
import ija.model.TileType;
import ija.util.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class HelpView {
    private Stage helpStage;
    private GridPane gridPane;
    private GameBoard gameBoard;

    public HelpView(Window owner, GameBoard board) {
        this.gameBoard = board;

        helpStage = new Stage();
        helpStage.initOwner(owner);
        helpStage.initModality(Modality.NONE);
        helpStage.setTitle("Nápověda");

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // mezery mezi dlaždicema
        gridPane.setHgap(1);
        gridPane.setVgap(1);

        // odsazení v okně
        gridPane.setPadding(new Insets(10));

        // obsah nápovědy
        showHelp();

        Scene scene = new Scene(gridPane);
        helpStage.setScene(scene);
    }

    // zobrazení nápovědy
    private void showHelp() {
        gridPane.getChildren().clear();

        if (gameBoard == null) {
            return;
        }

        for (int row = 0; row < gameBoard.getHeight(); row++) {
            for (int col = 0; col < gameBoard.getWidth(); col++) {
                Tile tile = gameBoard.getTile(row, col);
                StackPane cell = new StackPane();

                // velikost dlaždice
                cell.setPrefSize(Constants.TILE_SIZE, Constants.TILE_SIZE);

                Rectangle background = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
                background.setStroke(Color.LIGHTGRAY);

                // label pro nápovědu
                Label hintLabel = new Label();
                hintLabel.setFont(Font.font(14));
                hintLabel.setAlignment(Pos.CENTER);

                if (tile != null && tile.getType() != TileType.EMPTY) {
                    int rotationsNeeded = tile.getRotationsNeeded();

                    // nastavení počtu zbývajících otočení
                    hintLabel.setText(String.valueOf(rotationsNeeded));

                    // nastavení barvy pozadí podle zbývajících otočení
                    if (rotationsNeeded == 0) {
                        background.setFill(Color.LIGHTGREEN);
                    } else {
                        background.setFill(Color.LIGHTYELLOW);
                    }
                } else {
                    // prázdná dlaždice
                    background.setFill(Color.GHOSTWHITE);
                }

                cell.getChildren().addAll(background, hintLabel);
                gridPane.add(cell, col, row);
            }
        }
    }

    // zobrazení okna nápovědy
    public void showHelpWindow() {
        helpStage.show();
    }

    // aktualizace dle stavu desky
    public void updateHelp(GameBoard board) {
        this.gameBoard = board;
        showHelp();
    }

    // zavření okna nápovědy
    public void closeHelp() {
        helpStage.close();
    }

    // zda je okno nápovědy otevřené
    public boolean isHelpOpen() {
        return helpStage.isShowing();
    }
}
