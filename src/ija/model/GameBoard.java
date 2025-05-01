/**
 * Hrací deska obsahující dlaždice. 2D pole. 
 * List<List<Tile>>
 */

package ija.model;

import java.util.List;
import java.util.ArrayList;

public class GameBoard {
    private List<List<Tile>> board; // 2D pole dlaždic
    private int width; // šířka desky M
    private int height; // výška desky N
    private Tile sourceTile = null; // reference na dlaždici zdroje

    public GameBoard(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Rozměry desky musí být kladné.");
        }
        this.width = width;
        this.height = height;
        this.board = new ArrayList<>(height);
        initializeEmptyBoard();
    }

    // inicializace prázdné desky, naplnění dlaždicemi EMPTY
    private void initializeEmptyBoard() {
        for (int row = 0; row < height; row++) {
            List<Tile> currentRow = new ArrayList<>(width);

            for (int col = 0; col < width; col++) {
                currentRow.add(new Tile(TileType.EMPTY));
            }
            this.board.add(currentRow);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTile(int row, int col) {
        if (row >= 0 && row < height && col >= 0 && col < width) {
            return board.get(row).get(col);
        }
        return null;
    }

    // nastavení dlaždice na daných souřadnicích
    public void setTile(int row, int col, Tile tile) {
        if (row >= 0 && row < height && col >= 0 && col < width && tile != null) {
            board.get(row).set(col, tile);
        } else {
            System.err.println("Pokus o nastavení Tile mimo desku nebo null: " + row + "," + col);
        }
    }

    // nalezení zdrojové dlaždice
    public Tile getSourceTile() {
        if (sourceTile == null) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Tile tile = getTile(row, col);

                    if (tile != null && tile.getType() == TileType.SOURCE) {
                        sourceTile = tile;
                        return sourceTile;
                    }
                }
            }
        }

        return sourceTile;
    }

    // nalezení všech žárovek na desce
    public List<Tile> findAllBulbs() {
        List<Tile> bulbs = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tile tile = getTile(row, col);

                if (tile != null && tile.getType() == TileType.BULB) {
                    bulbs.add(tile);
                }
            }
        }

        return bulbs;
    }
}