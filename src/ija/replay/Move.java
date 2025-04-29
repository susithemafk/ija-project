/**
 * Struktura pro jednotlivé kroky v replayi.
 */
package ija.replay;

public class Move {
    private int x; // souřadnice x
    private int y; // souřadnice y
    private String state; // stav dlaždice po otočení

    public Move(int x, int y, String state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }
}