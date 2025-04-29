/**
 * Zaznamenává průběh hry do souboru pro pozdější přehrání.
 */
package ija.persistence;

public class GameLogger {
    private String filePath; // cesta k souboru
    private StringBuilder log; // logovací zprávy

    public GameLogger(String filePath) {
        this.filePath = filePath;
        this.log = new StringBuilder();
    }

    public void logMove(int row, int col, String newState) {
        // zaloguje tah do logu, asi ve formátu "row,col,newState"
    }
}