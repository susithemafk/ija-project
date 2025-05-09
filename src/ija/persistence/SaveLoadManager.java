package ija.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Stará se o "uložení" hry zkopírováním log souboru
 * a "načtení" hry nastavením log souboru pro replay.
 */
public class SaveLoadManager {

    public SaveLoadManager() {
    }

    /**
     * Uloží hru pomocí zkopírování aktuálního logu do nového souboru. Ten je pak
     * možné načíst.
     * 
     * @param logPath  Cesta k aktuálnímu logu.
     * @param savePath Cesta souboru k uložení.
     * @return True, pokud se uložení povedlo, jinak false.
     */
    public boolean saveGame(String logPath, String savePath) {
        File log = new File(logPath);
        File save = new File(savePath);

        // zkopírování logu do nového souboru
        try {
            Files.copy(log.toPath(), save.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Chyba při kopírování log souboru pro uložení: " + e.getMessage());
            return false;
        }
    }

    /**
     * Načte hru pomocí zkopírování uloženého souboru do aktuálního logu hry.
     * 
     * @param savePath Cesta k uloženému souboru.
     * @param logPath  Cesta k aktuálnímu logu.
     * @return True, pokud se log úspěšně přepsal, jinak false.
     */
    public boolean prepareLogForLoadingGame(String savePath, String logPath) {
        File save = new File(savePath);
        File log = new File(logPath);

        // zkopírování uloženého souboru do aktuálního logu
        try {
            Files.copy(save.toPath(), log.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Chyba při kopírování do aktuálního logu: " + e.getMessage());
            return false;
        }
    }
}