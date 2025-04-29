// TileView.java:
// Účel: JavaFX komponenta (např. potomek Pane nebo StackPane), která vizuálně reprezentuje jedno Tile.
// Funkce: Zobrazuje správný obrázek podle TileType, otáčí obrázek podle currentOrientation, mění vzhled (např. barvu pozadí) podle isPowered. Zpracovává kliknutí myší (setOnMouseClicked).

/**
 * Vizuální reprezentace dlaždice v herním poli.
 */

package ija.view;

public class TileView {
    private String image; // obrázek dlaždice
    private String poweredImage; // obrázek napájené dlaždice
    private int currentOrientation; // aktuální orientace dlaždice
}