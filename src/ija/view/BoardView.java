/**
 * Vizuální reprezentace hrací desky.
 */
package ija.view;

import ija.model.GameBoard;

public class BoardView {
    private int width; // šířka desky M
    private int height; // výška desky N
    private TileView[][] tiles; // 2D pole dlaždic
    private GameBoard gameBoard; // herní deska
}