/**
 * Dlaždice v herním poli.
 */
package ija.model;

public class Tile {
    private TileType type; // typ dlaždice
    private int x; // souřadnice dlaždice x
    private int y; // souřadnice dlaždice y
    private int currentOrientation; // aktuální orientace dlaždice
    private int correctOrientation; // správná orientace dlaždice
    private boolean isPowered; // zda je dlaždice napojena na zdroj
}