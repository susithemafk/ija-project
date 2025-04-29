/**
 * Načítá log soubor a převádí na sekvenci objektů. Udržuje ukazatel na aktuální krok v záznamu.
 * Pomocí metod se pohybuje v záznamu. 
 */
package ija.replay;

import java.util.List;

public class ReplayManager {
    private String logFilePath; // cesta k log souboru
    private int currentStep; // aktuální krok v záznamu
    private List<Move> moves; // seznam pohybů
}