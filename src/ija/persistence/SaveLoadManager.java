/**
 * Stará se o uložení a načtení stavu hry.
 */
package ija.persistence;

public class SaveLoadManager {
    private String filePath; // cesta k souboru

    public SaveLoadManager(String filePath) {
        this.filePath = filePath;
    }

    public void saveGame() {
        // Uloží stav hry do souboru
    }

    public void loadGame() {
        // Načte stav hry ze souboru
    }
}