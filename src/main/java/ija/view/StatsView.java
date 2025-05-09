package ija.view;

import java.util.Map;
import ija.model.GameBoard;
import ija.model.Tile;
import ija.model.TileType;
import ija.replay.ReplayManager;
import ija.util.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Zobrazuje okno se statistikami otočení pro každé políčko.
 */
public class StatsView {

	private Stage statsStage;
	private GridPane gridPane;
	private GameBoard gameBoard;
	private Map<String, Integer> rotationCounts; // počet otočení z logu

	public StatsView(Window owner, GameBoard board, ReplayManager logDataProvider) {
		gameBoard = board;

		// nastavení počtu otočení z logu
		logDataProvider.loadLog();
		rotationCounts = logDataProvider.getRotationStatsFromLog();

		// nový stage
		statsStage = new Stage();
		statsStage.initOwner(owner);
		statsStage.initModality(Modality.APPLICATION_MODAL);
		statsStage.setTitle("Statistiky otočení dlaždic");

		// nový gridPane
		gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(1);
		gridPane.setVgap(1);
		gridPane.setPadding(new Insets(10));

		populateStatsGrid();

		// nastavení nové scény
		Scene scene = new Scene(gridPane);
		statsStage.setScene(scene);
	}

	// naplnění gridPane statistikami
	private void populateStatsGrid() {
		// vyčištění gridPane před naplněním
		gridPane.getChildren().clear();

		// pro všechny dlaždice
		for (int row = 0; row < gameBoard.getHeight(); row++) {
			for (int col = 0; col < gameBoard.getWidth(); col++) {
				// tile
				Tile tile = gameBoard.getTile(row, col);
				StackPane cellPane = new StackPane();
				cellPane.setPrefSize(Constants.TILE_SIZE, Constants.TILE_SIZE);

				// tile background
				Rectangle background = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
				background.setStroke(Color.LIGHTGRAY);
				background.setFill(Color.GHOSTWHITE);

				VBox labelsContainer = new VBox(2);
				labelsContainer.setAlignment(Pos.CENTER);

				Label label = new Label("-");
				label.setFont(Font.font(10));

				// dlaždice, které lze otáčet
				if (tile != null && tile.getType() != TileType.EMPTY && tile.getType() != TileType.SOURCE) {
					String key = row + "," + col;
					int actualRotationCount = rotationCounts.getOrDefault(key, 0);

					label.setText(Integer.toString(actualRotationCount));
					background.setFill(Color.LIGHTGREEN);

				}
				// zdroj
				else if (tile != null && (tile.getType() == TileType.SOURCE)) {
					label.setText("SOURCE");
					background.setFill(Color.LIGHTGREEN);
				}
				// prázdné dlaždice
				else {
					background.setFill(Color.GHOSTWHITE);
				}

				// přidání popisků do VBoxu
				labelsContainer.getChildren().addAll(label);
				cellPane.getChildren().addAll(background, labelsContainer);
				gridPane.add(cellPane, col, row);
			}
		}
	}

	public void showStats() {
		if (!statsStage.isShowing()) {
			statsStage.showAndWait();
		}
	}
}