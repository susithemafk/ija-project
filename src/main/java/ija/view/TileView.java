/**
 * Vizuální reprezentace dlaždice v herním poli.
 */
package ija.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ija.model.Tile;
import ija.model.TileType;
import ija.util.Constants;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileView extends StackPane {
    private Tile tileData; // reference na data
    private Rectangle background; // pozadí
    private ImageView imageView; // obrázek

    private static final String IMAGES_PATH = "";

    public TileView(Tile tileData) {
        this.tileData = tileData;

        setPrefSize(Constants.TILE_SIZE, Constants.TILE_SIZE);
        setAlignment(Pos.CENTER);

        // nastavení pozadí
        background = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
        background.setFill(Color.GHOSTWHITE);
        background.setStroke(Color.LIGHTGRAY);

        // obrázek
        imageView = new ImageView();
        imageView.setFitWidth(Constants.TILE_SIZE);
        imageView.setFitHeight(Constants.TILE_SIZE);
        imageView.setPreserveRatio(true);

        // přidání pozadí a na to obrázek
        getChildren().addAll(background, imageView);

        // update vzhledu
        updateView();
    }

    // aktualizace vzhledu dlaždice
    public void updateView() {
        Image image = getImageForTile(tileData);

        if (image != null) {
            // nastavíme obrázek
            imageView.setImage(image);
            imageView.setRotate(tileData.getOrientation() * 90.0);
            imageView.setVisible(true);
        } else {
            // necháme jak je
        }
    }

    // získání obrázku pro danou dlaždici
    private Image getImageForTile(Tile tile) {
        TileType type = tile.getType();
        boolean isPowered = tile.isPowered();

        // empty nemá obrázek
        if (type == TileType.EMPTY) {
            return null;
        }

        // poskládání názvu obrázku
        String path = "";
        String suffix = isPowered ? "_on.png" : "_off.png";

        switch (type) {
            case L_PIPE:
                path = "pipes/L" + suffix;
                break;

            case I_PIPE:
                path = "pipes/I" + suffix;
                break;

            case T_PIPE:
                path = "pipes/T" + suffix;
                break;

            case X_PIPE:
                path = "pipes/X" + suffix;
                break;

            case SOURCE:
                path = "power.png";
                break;

            case BULB:
                path = "bulb" + suffix;
                break;

            default:
                return null;
        }

        String libPath = new File("lib").getAbsolutePath();
        String fullPath = libPath + File.separator + path;

        try {
            return new Image(new FileInputStream(fullPath));
        } catch (FileNotFoundException e) {
            System.err.println("Image not found: " + fullPath);
            return null;
        }
    }
}
