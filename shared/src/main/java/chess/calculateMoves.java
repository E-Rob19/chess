package chess;

import java.util.ArrayList;
//import java.util.Collection;

public class calculateMoves {
    //private final ChessPiece.PieceType type;
    public calculateMoves(){ //ChessPiece.PieceType type) {
        //this.type = type;
    }

    public ArrayList<ChessMove> calulate(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        if (type == ChessPiece.PieceType.BISHOP){
            movs = bishopMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.KING) {
            movs = kingMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.KNIGHT) {
            movs = knightMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.PAWN) {
            movs = pawnMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.QUEEN) {
            movs = queenMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.ROOK) {
            movs = rookMoves(board, myPosition);
        }
        return movs;
    }

    private ChessMove checkSpace(ChessBoard board, ChessPosition myPosition, ChessPosition pos){
        if (board.getPiece(pos) != null){
            if(board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                return new ChessMove(myPosition, pos, null);
            } else { return null; }
        } else { return new ChessMove(myPosition, pos, null); }
    }

    private ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessMove mov;
        while (row < 8 && col < 8) {
            row++; col++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row > 1 && col > 1) {
            row--; col--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row > 1 && col < 8) {
            row--; col++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row < 8 && col > 1) {
            row++; col--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        return movs;
    }

    private ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();

        return movs;
    }

    private ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();

        return movs;
    }

    private ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();

        return movs;
    }

    private ArrayList<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        movs.addAll(bishopMoves(board, myPosition));
        movs.addAll(rookMoves(board, myPosition));
        return movs;
    }

    private ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessMove mov;
        while (row < 8) {
            row++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row > 1) {
            row--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col < 8) {
            col++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col > 1) {
            col--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        return movs;
    }
}

//            if (board.getPiece(pos) != null){
//                if(board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
//                    movs.add(new ChessMove(myPosition, pos, null));
//                    break;
//                } else { break; }
//            } else { movs.add(new ChessMove(myPosition, pos, null)); }