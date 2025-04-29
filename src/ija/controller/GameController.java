/**
 * Prostředník mezi uživatelským rozhraním View a logikou hry. 
 * Zde by se mělo volat setupNewGame(), saveGame(), atd. 
 * Taky bysme zde měli řešit klik na dlaždici a říct GameManageru, aby otočil dlaždici. 
 */
package ija.controller;

import ija.model.GameManager;
import ija.view.BoardView;
import ija.view.MenuView;
import ija.view.StatusView;

public class GameController {
    private GameManager gameManager; // instance herního manažera
    private BoardView boardView; // instance vizuální reprezentace hrací desky
    private MenuView menuView; // instance vizuální reprezentace menu
    private StatusView statusView; // instance vizuální reprezentace stavu hry
}