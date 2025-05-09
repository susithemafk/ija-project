/**
 * Dlaždice v herním poli.
 */
package ija.model;

import ija.util.Side;
import java.util.EnumSet;

public class Tile {
    private final TileType type;
    private int orientation; // orientace dlaždice - 0:0°, 1:90°, 2:180°, 3:270°
    private int correctOrientation; // správná orientace dlaždice
    private boolean isPowered; // zda je dlaždice napojena na zdroj

    // výchozí nastavení dlaždice, později se přepisuje
    public Tile(TileType type) {
        this.type = type;
        this.orientation = 0;
        this.correctOrientation = 0;
        this.isPowered = false;
    }

    public TileType getType() {
        return type;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getCorrectOrientation() {
        return correctOrientation;
    }

    public boolean isPowered() {
        return isPowered;
    }


    public void setOrientation(int orientation) {
        if (orientation < 0) {
            orientation = orientation + 4;
        }

        if (orientation > 3) {
            orientation = orientation - 4;
        }

        this.orientation = orientation;
    }

    public void setCorrectOrientation(int correctOrientation) {
        if (correctOrientation < 0) {
            correctOrientation = correctOrientation + 4;
        }

        if (correctOrientation > 3) {
            correctOrientation = correctOrientation - 4;
        }

        this.correctOrientation = correctOrientation;
    }

    public void setPowered(boolean powered) {
        this.isPowered = powered;
    }

    // otočení dlaždice o 90° doprava
    public void rotate() {
        this.setOrientation(this.orientation + 1);
    }

    // potřebný počet otočení k dosažení správné orientace
    public int getRotationsNeeded() {
        // rozdíl mezi aktuální a správnou orientací
        int difference = this.correctOrientation - this.orientation;

        // pokud je záporný +4
        if (difference < 0) {
            difference += 4;
        }

        // I pipe může mít max 1 otočení pro správnou orientaci
        if (this.type == TileType.I_PIPE) {
            if (difference == 1 || difference == 3) {
                return 1;
            }
            if (difference == 2) {
                return 0;
            }
        }

        // zbytek po dělení 4
        return difference % 4;
    }

    /**
     * hlavní metoda pro zjištění,
     * které strany jsou propojené při daném typu a aktuální orientaci
     */
    public EnumSet<Side> getConnectedSides() {
        EnumSet<Side> sides = EnumSet.noneOf(Side.class);
        int rotation = this.orientation;

        switch (this.type) {
            // tvar L
            case L_PIPE:
                if (rotation == 0)
                    sides.addAll(EnumSet.of(Side.NORTH, Side.EAST));
                else if (rotation == 1)
                    sides.addAll(EnumSet.of(Side.EAST, Side.SOUTH));
                else if (rotation == 2)
                    sides.addAll(EnumSet.of(Side.SOUTH, Side.WEST));
                else if (rotation == 3)
                    sides.addAll(EnumSet.of(Side.WEST, Side.NORTH));
                break;

            // tvar I
            case I_PIPE:
                if (rotation == 0 || rotation == 2)
                    sides.addAll(EnumSet.of(Side.NORTH, Side.SOUTH));
                else if (rotation == 1 || rotation == 3)
                    sides.addAll(EnumSet.of(Side.EAST, Side.WEST));
                break;

            // tvar T
            case T_PIPE:
                if (rotation == 0)
                    sides.addAll(EnumSet.of(Side.SOUTH, Side.EAST, Side.WEST));
                else if (rotation == 1)
                    sides.addAll(EnumSet.of(Side.WEST, Side.SOUTH, Side.NORTH));
                else if (rotation == 2)
                    sides.addAll(EnumSet.of(Side.EAST, Side.WEST, Side.NORTH));
                else if (rotation == 3)
                    sides.addAll(EnumSet.of(Side.SOUTH, Side.NORTH, Side.EAST));
                break;

            // tvar X a zdroj
            case X_PIPE:
            case SOURCE:
                sides.addAll(EnumSet.of(Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST));
                break;

            // žárovka jde připojit pouze ze severu
            case BULB:
                sides.add(Side.NORTH);
                break;

            case EMPTY:
                break;
        }
        return sides;
    }
}