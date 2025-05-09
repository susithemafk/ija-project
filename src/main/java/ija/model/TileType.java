/**
 * Definice možných typů dlaždic
 */
package ija.model;

public enum TileType {
    L_PIPE, I_PIPE, T_PIPE, X_PIPE, SOURCE, BULB, EMPTY;

    public static TileType fromString(String type) {
        switch (type) {
            case "L_PIPE":
                return L_PIPE;
            case "I_PIPE":
                return I_PIPE;
            case "T_PIPE":
                return T_PIPE;
            case "X_PIPE":
                return X_PIPE;
            case "SOURCE":
                return SOURCE;
            case "BULB":
                return BULB;
            case "EMPTY":
                return EMPTY;
            default:
                throw new IllegalArgumentException("Unknown tile type: " + type);
        }
    }
}