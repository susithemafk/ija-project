/**
 * Hrací deska obsahující dlaždice. 2D pole. 
 * List<List<Tile>>
 */

package ija.model;

import java.util.List;

public class GameBoard {
    private List<List<Tile>> board; // 2D pole dlaždic
    private int width; // šířka desky M
    private int height; // výška desky N
}
