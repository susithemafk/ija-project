package ija.replay;

import ija.model.GameBoard;
import ija.model.Tile;
import ija.model.TileType;
import ija.util.Constants;
import ija.util.Side;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Načítá log soubor, umožňuje krokování.
 */
public class ReplayManager {
    private String logFilePath;
    private List<MoveRecord> recordedMoves; // Záznamy tahů
    private GameBoard gameBoard; // Počáteční stav desky pro replay
    private int boardWidth;
    private int boardHeight;
    public int currentReplayStep = -1; // -1 znamená stav před prvním tahem (gameBoard)
    public String originalGameStartTime; // Čas z GAME_START
    public String originalInitialSetupString; // Kompletní INITIAL_SETUP string

    public List<MoveRecord> getParsedMoves() {
        return new ArrayList<>(recordedMoves);
    }

    public record MoveRecord(int row, int col, int newOrientation) {
    }

    public ReplayManager(String logFilePath) {
        this.logFilePath = logFilePath;
        this.recordedMoves = new ArrayList<>();
    }

    /**
     * Načte data z logovacího souboru.
     *
     * @return True, pokud se log úspěšně načetl, jinak false.
     */
    public boolean loadLog() {
        recordedMoves.clear();
        currentReplayStep = -1;
        boardWidth = Constants.DEFAULT_WIDTH;
        boardHeight = Constants.DEFAULT_HEIGHT;
        originalGameStartTime = "";
        originalInitialSetupString = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            boolean initialSetupProcessed = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 2); // Rozdělit jen podle prvního středníku
                if (parts.length > 0) {
                    switch (parts[0]) {
                        case "GAME_START":
                            if (parts.length > 1)
                                originalGameStartTime = parts[1];
                            break;
                        case "BOARD_SIZE":
                            if (parts.length > 1) {
                                String[] dims = parts[1].split(";");
                                if (dims.length == 2) {
                                    boardWidth = Integer.parseInt(dims[0]);
                                    boardHeight = Integer.parseInt(dims[1]);
                                }
                            }
                            break;
                        case "INITIAL_SETUP":
                            if (parts.length > 1) {
                                originalInitialSetupString = parts[1]; // Uložíme celý řetězec
                                gameBoard = parseInitialBoard(originalInitialSetupString, boardWidth,
                                        boardHeight);
                                initialSetupProcessed = true;
                            }
                            break;
                        case "MOVE":
                            if (initialSetupProcessed && parts.length > 1) { // MOVE parsujeme až po INITIAL_SETUP
                                String[] moveParts = parts[1].split(";");
                                if (moveParts.length == 3) {
                                    int row = Integer.parseInt(moveParts[0]);
                                    int col = Integer.parseInt(moveParts[1]);
                                    int newOrientation = Integer.parseInt(moveParts[2]);
                                    recordedMoves.add(new MoveRecord(row, col, newOrientation));
                                }
                            }
                            break;
                        // GAME_END můžeme ignorovat pro replay
                    }
                }
            }
            return gameBoard != null; // Log je platný, pokud má alespoň initial setup
        } catch (IOException | NumberFormatException e) {
            System.err.println("Chyba při načítání log souboru pro replay: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rozparsuje string s počátečním nastavením desky.
     * Formát: "r1,c1,TYPE1,correctOrientation1|r2,c2,TYPE2,correctOrientation2|..."
     */
    private GameBoard parseInitialBoard(String setupString, int width, int height) {
        GameBoard board = new GameBoard(width, height);
        String[] tileEntries = setupString.split("\\|");
        for (String entry : tileEntries) {
            String[] parts = entry.split(",");
            if (parts.length == 5) { // Očekáváme 5 částí
                try {
                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    TileType type = TileType.valueOf(parts[2].trim());
                    int correctOrientation = Integer.parseInt(parts[3].trim());
                    int initialCurrentOrientation = Integer.parseInt(parts[4].trim());
                    Tile tile = new Tile(type);
                    tile.setCorrectOrientation(correctOrientation);
                    tile.setOrientation(initialCurrentOrientation);
                    board.setTile(r, c, tile);
                } catch (IllegalArgumentException e) {
                    System.err
                            .println("Chyba při parsování initial setup (5 částí): " + entry + " - " + e.getMessage());
                }
            } else {
                System.err.println("Chybný formát v initial setup (očekáváno 5 částí): " + entry);
            }
        }
        // Není potřeba zde volat updatePowerState, getCurrentBoardState to udělá
        return board;
    }

    /**
     * Vytvoří novou herní desku pro replay - kopii aktuální herní desky.
     * 
     * @return Kopie herní desky.
     */
    public GameBoard getCurrentBoardState() {
        // vytvoření kopie herní desky
        GameBoard replayBoard = new GameBoard(this.gameBoard.getWidth(), this.gameBoard.getHeight());
        for (int row = 0; row < this.gameBoard.getHeight(); row++) {
            for (int col = 0; col < this.gameBoard.getWidth(); col++) {
                // dlaždice z herní desky
                Tile tile = this.gameBoard.getTile(row, col);

                if (tile != null) {
                    // vytvoření nové dlaždice - kopie
                    Tile newTile = new Tile(tile.getType());
                    newTile.setCorrectOrientation(tile.getCorrectOrientation());
                    newTile.setOrientation(tile.getOrientation());
                    replayBoard.setTile(row, col, newTile);
                }
            }
        }

        // aplikace tahů
        for (int i = 0; i <= this.currentReplayStep && i < this.recordedMoves.size(); i++) {
            MoveRecord move = this.recordedMoves.get(i);
            Tile tileToUpdate = replayBoard.getTile(move.row(), move.col());
            if (tileToUpdate != null) {
                tileToUpdate.setOrientation(move.newOrientation());
            }
        }

        updatePowerStateForBoard(replayBoard);
        return replayBoard;
    }

    /**
     * aktualizace isPowered pro všechny dlaždice od zdroje dle BFS,
     * kopie z GameManager
     */
    private void updatePowerStateForBoard(GameBoard board) {
        Tile source = board.getSourceTile();

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
        for (int r = 0; r < board.getHeight(); ++r) {
            for (int c = 0; c < board.getWidth(); ++c) {
                if (board.getTile(r, c) == source) {
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
            Tile currentTile = board.getTile(row, col);

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
                Tile neighbor = board.getTile(neighRow, neighCol);
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
        for (int row = 0; row < board.getHeight(); row++)
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);

                if (tile != null) {
                    boolean isPowered = newlyPowered.contains(tile);

                    if (tile.isPowered() != isPowered) {
                        tile.setPowered(isPowered);
                    }
                }
            }
    }

    /**
     * Přepíše log hry obsahem z replaye.
     * 
     * @param logPath Cesta k logu
     * @return true pokud úspěšné, jinak false.
     */
    public boolean writeCurrentReplayStateToLog(String logPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logPath, false))) {

            // ponechání původního hlavičky logu
            writer.println("GAME_START;" + this.originalGameStartTime);
            writer.println("BOARD_SIZE;" + this.boardWidth + ";" + this.boardHeight);
            writer.println("INITIAL_SETUP;" + this.originalInitialSetupString);

            // zapsání tahů až do aktuálního
            for (int i = 0; i <= currentReplayStep && i < recordedMoves.size(); i++) {
                MoveRecord move = recordedMoves.get(i);
                writer.println("MOVE;" + move.row() + ";" + move.col() + ";" + move.newOrientation());
            }

            return true;
        } catch (IOException e) {
            System.err.println("Chyba přepsání logu replayem: " + e.getMessage());
            return false;
        }
    }

    /**
     * Pro každé políčko vrátí počet otočení.
     * Klíč: String - "row,col"
     * Hodnota: Integer - počet otočení
     * 
     * @return Mapa s počtem otočení pro každé políčko.
     */
    public Map<String, Integer> getRotationStatsFromLog() {
        Map<String, Integer> rotationCounts = new HashMap<>();

        if (recordedMoves == null || recordedMoves.isEmpty()) {
            return rotationCounts;
        }

        for (MoveRecord move : recordedMoves) {
            String key = move.row() + "," + move.col();
            rotationCounts.put(key, rotationCounts.getOrDefault(key, 0) + 1);
        }
        return rotationCounts;
    }

    public boolean hasNextStep() {
        return currentReplayStep < recordedMoves.size() - 1;
    }

    public boolean hasPreviousStep() {
        return currentReplayStep >= 0;
    }

    public boolean nextStep() {
        if (hasNextStep()) {
            currentReplayStep++;
            return true;
        }

        return false;
    }

    public boolean previousStep() {
        if (hasPreviousStep()) {
            currentReplayStep--;
            return true;
        }

        return false;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

}
