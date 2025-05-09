package ija.persistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Zaznamenává průběh hry do souboru pro pozdější přehrání.
 */
public class GameLogger {
    private String filePath;

    public GameLogger(String filePath) {
        this.filePath = filePath;

        // prázdný logovací soubor
        clearLogFile();
    }

    // vyčistí logovací soubor
    private void clearLogFile() {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.print("");
        } catch (IOException e) {
            System.err.println("Chyba při čištění logovacího souboru: " + e.getMessage());
        }
    }

    /**
     * Zaloguje start nové hry, např. s rozměry desky.
     * 
     * @param width             Šířka desky.
     * @param height            Výška desky.
     * @param initialBoardState Reprezentace počátečního stavu desky (např. string).
     */
    public void logGameStart(int width, int height, String initialBoardState) {
        // Vyčištění se teď bude volat explicitně z GameManager.setupNewGame přes
        // resetLogger
        try (FileWriter fw = new FileWriter(filePath, false); // false pro přepsání (pokud je to první zápis po resetu)
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            out.println("GAME_START;" + dtf.format(LocalDateTime.now()));
            out.println("BOARD_SIZE;" + width + ";" + height);
            out.println("INITIAL_SETUP;" + initialBoardState);
        } catch (IOException e) {
            // ...
        }
    }

    /**
     * Zaloguje tah hráče (otočení políčka).
     * 
     * @param row            Řádek otočeného políčka.
     * @param col            Sloupec otočeného políčka.
     * @param newOrientation Nová orientace políčka po otočení.
     */
    public void logMove(int row, int col, int newOrientation) {
        try (FileWriter fw = new FileWriter(filePath, true); // true pro append
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            // Formát: "MOVE;řádek;sloupec;nová_orientace"
            out.println("MOVE;" + row + ";" + col + ";" + newOrientation);
        } catch (IOException e) {
            System.err.println("Chyba při logování tahu: " + e.getMessage());
        }
    }

    /**
     * Zaloguje konec hry (např. výhra).
     */
    public void logGameEnd() {
        try (FileWriter fw = new FileWriter(filePath, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println("GAME_END");
        } catch (IOException e) {
            System.err.println("Chyba při logování konce hry: " + e.getMessage());
        }
    }

    public void resetAndClearLog() { // Nová metoda nebo přejmenovat clearLogFile a udělat public
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.print(""); // Přepíše soubor prázdným obsahem
            System.out.println("GameLogger: Log soubor vyčištěn: " + filePath);
        } catch (IOException e) {
            System.err.println("Chyba při čištění logovacího souboru: " + e.getMessage());
        }
    }

}
