package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
        //throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        String pos = "";

        if(col == 1){
            pos += "a";
//        } else if (col == 2) {
//            pos += "b";
//        } else if (col == 3) {
//            pos += "c";
//        }else if (col == 4) {
//            pos += "d";
//        }else if (col == 5) {
//            pos += "e";
//        }else if (col == 6) {
//            pos += "f";
//        }else if (col == 7) {
//            pos += "g";
//        }else if (col == 8) {
//            pos += "h";
        }
        pos += row;
        //return pos;

        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
