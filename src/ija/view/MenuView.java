/**
 * Vizuální reprezentace menu v herním okně.
 */
package ija.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class MenuView {
    private MenuBar menuBar;
    private MenuItem newGameItem;
    private MenuItem saveGameItem;
    private MenuItem loadGameItem;
    private MenuItem showHintItem;
    private MenuItem exitItem;
    private MenuItem replayGameItem;

    public MenuView() {
        // vytvoření menubaru
        menuBar = new MenuBar();

        // vytvoření menu
        Menu gameMenu = new Menu("Hra");

        // vytvoření menuitemů
        newGameItem = new MenuItem("Nová hra");
        saveGameItem = new MenuItem("Uložit hru");
        loadGameItem = new MenuItem("Načíst hru");
        showHintItem = new MenuItem("Zobrazit nápovědu");
        replayGameItem = new MenuItem("Přehrát poslední hru");
        exitItem = new MenuItem("Konec");

        // přidání položek do menu
        gameMenu.getItems().addAll(
                newGameItem,
                saveGameItem, // Přidáno
                loadGameItem, // Přidáno
                new SeparatorMenuItem(),
                showHintItem,
                replayGameItem,
                new SeparatorMenuItem(),
                exitItem);

        // přidání menu do menubaru
        menuBar.getMenus().add(gameMenu);

        // funkcionalita menu implementována v GameControlleru
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public MenuItem getNewGameItem() {
        return newGameItem;
    }

    public MenuItem getSaveGameItem() {
        return saveGameItem;
    } // Getter

    public MenuItem getLoadGameItem() {
        return loadGameItem;
    } // Getter

    public MenuItem getShowHintItem() {
        return showHintItem;
    }

    public MenuItem getReplayGameItem() {
        return replayGameItem;
    }

    public MenuItem getExitItem() {
        return exitItem;
    }
}