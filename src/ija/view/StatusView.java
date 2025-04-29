/**
 * Vizuální reprezentace stavu hry.
 */
package ija.view;

public class StatusView {
    private int stepCount; // počet kroků
    private String message; // zpráva o výhře nebo prohře
    private long timer; // časovač

    public StatusView() {
        this.stepCount = 0;
        this.message = "";
        this.timer = 0;
    }
}