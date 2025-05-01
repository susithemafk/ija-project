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
        exitItem = new MenuItem("Konec");

        // separátor
        SeparatorMenuItem separator = new SeparatorMenuItem();

        // přidání položek do menu
        gameMenu.getItems().addAll(newGameItem, separator, exitItem);

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

    public MenuItem getExitItem() {
        return exitItem;
    }

}