/**
 * Třída pro logiku hry. 
 */
package ija.model;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import ija.util.Constants;
import ija.util.Side;
import ija.persistence.GameLogger;
import ija.replay.ReplayManager;
import ija.replay.ReplayManager.MoveRecord;

public class GameManager {
    private GameBoard gameBoard; // hrací deska
    private int countOfSteps; // počet provedených kroků
    private GameLogger logger; // logger pro ukládání průběhu hry

    public GameManager() {
        this.countOfSteps = 0;

        // nový logger
        this.logger = new GameLogger(Constants.LOG_FILE_NAME);

    }

    public void setupNewGame() {
        System.out.println("GameManager: Vytváření nové hry...");

        this.gameBoard = new GameBoard(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
        this.countOfSteps = 0;

        // vytvoření dlaždic
        Tile source = new Tile(TileType.SOURCE);
        Tile t11 = new Tile(TileType.T_PIPE);
        t11.setCorrectOrientation(2);
        Tile t10 = new Tile(TileType.L_PIPE);
        t10.setCorrectOrientation(1);
        Tile t20 = new Tile(TileType.I_PIPE);
        t20.setCorrectOrientation(0);
        Tile bulb30 = new Tile(TileType.BULB);
        Tile t12 = new Tile(TileType.L_PIPE);
        t12.setCorrectOrientation(2);
        Tile t22 = new Tile(TileType.I_PIPE);
        t22.setCorrectOrientation(0);
        Tile bulb32 = new Tile(TileType.BULB);

        // umístění na desku
        gameBoard.setTile(0, 1, source);
        gameBoard.setTile(1, 1, t11);
        gameBoard.setTile(1, 0, t10);
        gameBoard.setTile(2, 0, t20);
        gameBoard.setTile(3, 0, bulb30);
        gameBoard.setTile(1, 2, t12);
        gameBoard.setTile(2, 2, t22);
        gameBoard.setTile(3, 2, bulb32);

        // náhodné otočení a nastavení currentOrientation - otočení 0-3
        java.util.Random random = new java.util.Random();
        for (int r = 0; r < gameBoard.getHeight(); r++) {
            for (int c = 0; c < gameBoard.getWidth(); c++) {
                Tile tile = gameBoard.getTile(r, c);

                // jen neprázdné dlaždice a ne zdroj a žárovka
                if (tile != null && tile.getType() != TileType.EMPTY && tile.getType() != TileType.SOURCE
                        && tile.getType() != TileType.BULB) {

                    int randomOrientation = random.nextInt(4);
                    tile.setOrientation(randomOrientation);
                }
            }
        }

        // přidání zdroje do loggeru
        logger.logGameStart(gameBoard.getWidth(), gameBoard.getHeight(),
                generateInitialSetupStringFromBoard(this.gameBoard));

        // přepočítání počátečního stavu napájení
        updatePowerState();

        System.out.println("GameManager: deska vytvořena a napájení aktualizováno.");
    }

    public void rotateTile(int row, int col) {
        Tile tile = gameBoard.getTile(row, col);

        // Otočit lze jen neprázdné dlaždice a ne zdroj a žárovku
        if (tile != null && tile.getType() != TileType.EMPTY && tile.getType() != TileType.SOURCE
                && tile.getType() != TileType.BULB) {

            // otočení
            tile.rotate();

            // přičtení kroku
            this.countOfSteps++;

            System.out.println("GameManager: Otáčím políčko na [" + row + "," + col + "], rotace: "
                    + tile.getOrientation());

            // zalogování
            logger.logMove(row, col, tile.getOrientation());

            // přepočítání
            updatePowerState();
        }
    }

    // aktualizace isPowered pro všechny dlaždice od zdroje dle BFS
    public void updatePowerState() {
        Tile source = gameBoard.getSourceTile();

        // navštívené dlaždice
        Set<Tile> visited = new HashSet<>();

        // fronta
        record RowCol(int row, int col) {
        }
        Queue<RowCol> queue = new ArrayDeque<>();

        // napájené dlaždice
        Set<Tile> newlyPowered = new HashSet<>();

        // nalezení zdroje
        int startRow = -1;
        int startCol = -1;
        for (int r = 0; r < gameBoard.getHeight(); ++r) {
            for (int c = 0; c < gameBoard.getWidth(); ++c) {
                if (gameBoard.getTile(r, c) == source) {
                    startRow = r;
                    startCol = c;
                    break;
                }
            }
            if (startRow != -1)
                break;
        }

        // přidání do fronty
        queue.add(new RowCol(startRow, startCol));

        // přidání dlaždice do visited
        visited.add(source);

        // přidání zdroje do newlyPowered - je vždy napájený
        newlyPowered.add(source);

        while (!queue.isEmpty()) {
            RowCol currentCoords = queue.poll();
            int row = currentCoords.row();
            int col = currentCoords.col();
            Tile currentTile = gameBoard.getTile(row, col);

            // pro všechny strany kam vede proud z currentTile
            for (Side side : currentTile.getConnectedSides()) {
                // souřadnice souseda
                int neighRow = row;
                int neighCol = col;

                // strana, ze které proud přichází k sousedovi
                Side oppositeSide;

                // nastavení hodnot dle strany
                switch (side) {
                    case NORTH:
                        neighRow--;
                        oppositeSide = Side.SOUTH;
                        break;
                    case EAST:
                        neighCol++;
                        oppositeSide = Side.WEST;
                        break;
                    case SOUTH:
                        neighRow++;
                        oppositeSide = Side.NORTH;
                        break;
                    case WEST:
                        neighCol--;
                        oppositeSide = Side.EAST;
                        break;
                    default:
                        return;
                }

                // kontrola, zda je soused na desce:
                // zda sousední dlaždice existuje && zda ještě nebyla přidána do visited
                Tile neighbor = gameBoard.getTile(neighRow, neighCol);
                if (neighbor != null && !visited.contains(neighbor)) {

                    // kontrola, zda je soused připojen na stranu odkud přišel proud
                    if (neighbor.getConnectedSides().contains(oppositeSide)) {

                        // soused je připojen a ještě nebyl navštíven
                        visited.add(neighbor);
                        newlyPowered.add(neighbor);

                        // přidání souseda do fronty
                        RowCol neigh = new RowCol(neighRow, neighCol);
                        queue.add(neigh);
                    }
                }
            }
        }

        // aktualizace stavu napájení
        for (int row = 0; row < gameBoard.getHeight(); row++) {
            for (int col = 0; col < gameBoard.getWidth(); col++) {
                Tile tile = gameBoard.getTile(row, col);

                if (tile != null) {
                    boolean isPowered = newlyPowered.contains(tile);

                    if (tile.isPowered() != isPowered) {
                        tile.setPowered(isPowered);
                    }
                }
            }
        }
    }

    public int getCountOfSteps() {
        return countOfSteps;
    }

    public GameBoard getBoard() {
        return gameBoard;
    }

    // kontrola výherní podmínky
    public boolean checkWinCondition() {
        List<Tile> bulbs = gameBoard.findAllBulbs();

        // alespoň jedna žárovka není napájená = prohra
        for (Tile bulb : bulbs) {
            if (!bulb.isPowered()) {
                return false;
            }
        }

        // log konce hry
        logger.logGameEnd();
        System.out.println("GameManager: Výherní podmínka splněna!");

        return true;
    }

    /**
     * Načte hru z logu.
     * Logika je taková, že uložená hra je uložen log hry, ten se zde načte a
     * provede se otočení dlaždic dle příkazů MOVE a přepsání aktuální desky.
     */
    public void loadGameFromLog() {
        ReplayManager tempReplayManager = new ReplayManager(Constants.LOG_FILE_NAME);

        // načtení logu
        if (tempReplayManager.loadLog()) {
            GameBoard tempBoard = tempReplayManager.getGameBoard();
            List<MoveRecord> movesFromLog = tempReplayManager.getParsedMoves();

            // pro všechny příkazy MOVE se provede otočení dlaždice a zapsání kroku
            this.countOfSteps = 0;
            for (MoveRecord move : movesFromLog) {
                Tile tileToUpdate = tempBoard.getTile(move.row(), move.col());
                if (tileToUpdate != null &&
                        tileToUpdate.getType() != TileType.EMPTY &&
                        tileToUpdate.getType() != TileType.SOURCE &&
                        tileToUpdate.getType() != TileType.BULB) {

                    // správné otočení
                    tileToUpdate.setOrientation(move.newOrientation());

                    // přičtení kroku
                    this.countOfSteps++;
                }
            }

            this.gameBoard = tempBoard;

            updatePowerState();
            System.out.println("GameManager: Hra načtena z logu, počet kroků: " + this.countOfSteps);
        } else {
            System.err.println("GameManager: Chyba při načítání logu pro obnovení hry. Spouštím novou hru.");
            setupNewGame();
        }
    }

    /**
     * Generuje string initial setupu hrací desky.
     * Formát: řádek, sloupec, typ dlaždice, správná orientace, aktuální orientace.
     * 
     * @example 0,1,SOURCE,0,0|1,0,L_PIPE,1,0|...
     */
    private String generateInitialSetupStringFromBoard(GameBoard board) {
        if (board == null)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);

                if (tile != null && tile.getType() != TileType.EMPTY) {
                    if (sb.length() > 0) {
                        sb.append("|");
                    }

                    sb.append(row).append(",").append(col).append(",")
                            .append(tile.getType().toString()).append(",")
                            .append(tile.getCorrectOrientation()).append(",")
                            .append(tile.getOrientation());
                }
            }
        }

        return sb.toString();
    }

    public void setBoard(GameBoard board) {
        this.gameBoard = board;
        updatePowerState();
    }

    public void setCountOfSteps(int steps) {
        this.countOfSteps = steps;
    }
}