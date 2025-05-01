/**
 * Vizuální reprezentace hrací desky.
 */
package ija.view;

import ija.model.GameBoard;
import ija.model.Tile;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class BoardView {
    private GridPane gridPane;

    public BoardView() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // nastavení mezer mezi dlaždicemi
        gridPane.setHgap(1);
        gridPane.setVgap(1);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    // vykreslení hrací desky dle stavu GameBoard
    public void drawBoard(GameBoard board) {
        // vyčištění GridPane
        gridPane.getChildren().clear();

        // vytvoření TileView pro každou dlaždici na desce
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);
                TileView tileView = new TileView(tile);

                // přidání TileView do GridPane
                gridPane.add(tileView, col, row);
            }
        }
    }

    // aktualizace vzhledu všech TileView
    public void updateAllTiles() {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof TileView) {
                ((TileView) node).updateView();
            }
        }
    }
}
